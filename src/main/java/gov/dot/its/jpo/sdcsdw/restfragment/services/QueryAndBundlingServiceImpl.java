package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.RawPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResult;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

@Service
@Primary
public class QueryAndBundlingServiceImpl implements QueryAndBundlingService{

	private QueryService queryService;
	private BundlingService bundleService;
	
	@Autowired
	public QueryAndBundlingServiceImpl(QueryService queryService, BundlingService bundleService) {
		this.queryService = queryService;
		this.bundleService = bundleService;
	}

	//First query through query service
	@Override
	public QueryResult queryAndBundle(Query query) throws CodecFailedException, FormattingFailedException, UnformattingFailedException, IOException, DecoderException, JAXBException, InvalidQueryException {
		// TODO Auto-generated method stub
		
		//Perform query validation
		queryService.validateQuery(query);
		
		//Perform the query. The results returned are encoded based on the 
		//resultEncoding parameter in the query (i.e., full, base64, hex)
		List<String> queryResults = queryService.forwardQuery(query);
		
		//Check the result packaging query parameter. If none, package into
		//QueryResults and return. If packaging is bundle or distribution,
		//format the results into AdvisorySituationData objects, and call
		//bundling service.
		
		QueryResult qr = new QueryResult();
		
		//If the result packaging parameter is not null and does not equal none
		if(query.getResultPackaging() != null && !query.getResultPackaging().equals("none")) {
			//create ASD list and bundle
			List<AdvisorySituationData> asdList = convertResultsToASDList(queryResults, query);
			
			//Call BundleService to get bundle or distribution
			//Either get back query result or bundle/distribution. If the latter, convert to query result.
			
		} else {
			String[] resultsAsArray = queryResults.toArray(new String[0]);
			qr.setResults(resultsAsArray);
		}
		
		
		return qr;
	}
	
	private List<AdvisorySituationData> convertResultsToASDList(List<String> queryResults, Query query) throws IOException, DecoderException, 
				CodecFailedException, FormattingFailedException, UnformattingFailedException, JAXBException {
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		
		//Determine how the results are encoded from query parameter
		String resultEncoding = "hex";
		if(query.getResultEncoding() != null)
			resultEncoding = query.getResultEncoding();
		
		//For each queryResult
		for(String queryResult : queryResults) {
			
			String encodedMsgAsHex = null;
			if(resultEncoding.equalsIgnoreCase("hex")) {
				//Do nothing, already in necessary format
				encodedMsgAsHex = queryResult;
				
			} else if(resultEncoding.equalsIgnoreCase("base64")) {
				//convert to hex
				encodedMsgAsHex = Hex.encodeHexString(Base64.decodeBase64(queryResult));
				
			} else if(resultEncoding.equalsIgnoreCase("full")) {
				//extract encodedMsg
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode node = mapper.readTree(queryResult);
					String encodedMsg = node.findValue("encodedMsg").textValue();
					
					//Convert encodedMsg into hex (default stored as base64)
					encodedMsgAsHex = Hex.encodeHexString(Base64.decodeBase64(encodedMsg));
				} catch (IOException e) {
					//TODO log inability to parse full string
					throw e;
				}
			}
			
			AdvisorySituationData asd = convertResultToASD(encodedMsgAsHex);
			asdList.add(asd);
		}

		
		return asdList;
	}
	
	//Query result must be converted to hex (encodedMsg as hex equivalent to hex encoded ASD)
	private AdvisorySituationData convertResultToASD(String hexEncodedASD) throws DecoderException, CodecFailedException, 
				FormattingFailedException, UnformattingFailedException, JAXBException {
		
		AdvisorySituationData asd = null;
		byte[] hexEncodedASDAsByte = null;
		try {
			hexEncodedASDAsByte = Hex.decodeHex(hexEncodedASD);
		} catch (DecoderException e) {
			//TODO log exception
			throw e;
		}
		
		try {
			//Decode byte array to XML
			String xerEncodedASD = (PerXerCodec.perToXer(Asn1Types.getAsn1TypeByName("AdvisorySituationData"),
					hexEncodedASDAsByte, RawPerData.unformatter, RawXerData.formatter));
			
			// Convert XML to POJO
			asd = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(xerEncodedASD);

		} catch (CodecFailedException | FormattingFailedException | UnformattingFailedException | JAXBException e) {
			//TODO log exception
			//logger.error("Failed to decode and convert to POJO ASD retrieved from Mongo", e);
			throw e;
		}
		
		return asd;
	}
	
}
