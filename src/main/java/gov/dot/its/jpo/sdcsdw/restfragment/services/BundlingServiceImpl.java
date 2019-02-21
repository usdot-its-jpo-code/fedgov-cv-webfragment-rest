package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistributionList;
import gov.dot.its.jpo.sdcsdw.Models.AsdBundles;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.HexPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.udpdialoghandler.service.MessageCreator;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;




@Service
public class BundlingServiceImpl implements BundlingService {
    private final static Logger logger = LoggerFactory.getLogger(BundlingServiceImpl.class);
    
	@Override
	public List<JsonNode> bundleOrDistribute(List<JsonNode> jsonList, String packageType, String dialogId) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		List<JsonNode> returnJsons;
		List<AdvisorySituationData> asdList;
		switch(packageType) {
		case "bundle":
		    logger.info("Bundling");
		    returnJsons = new ArrayList<>();
		    asdList = decodeAsds(jsonList);
		    List<AdvisorySituationBundle> bundleList = createBundleList(asdList, dialogId);
		    for (AdvisorySituationBundle bundle : bundleList) {
                returnJsons.add(mapper.valueToTree(bundle));
            }
		    break;
		case "distribution":
		    logger.info("Distributing");
		    returnJsons = new ArrayList<>();
		    asdList = decodeAsds(jsonList);
		    List<AdvisorySituationDataDistribution> distributionList = createDistributionList(asdList, dialogId);
		    for (AdvisorySituationDataDistribution distribution : distributionList) {
		        JsonNode node = mapper.valueToTree(distribution);
		        logger.info("Got node " + node.toString());
                returnJsons.add(node);
            }
		    break;
	    default:
	        logger.info("No Packaging");
	        returnJsons = jsonList;
	        break;
		}
		
		return returnJsons;
	}
	
	private List<AdvisorySituationData> decodeAsds(List<JsonNode> jsonList) {
	    List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
	    
	    for (JsonNode json : jsonList) {
            //To be changed, currently implementing alternative to this is encoding service
            //AdvisorySituationData asd = mapper.treeToValue(json, AdvisorySituationData.class);
            String encodedMsgPerHex = json.get("encodedMsg").asText();
            try {
                String encodedMsgXerString = PerXerCodec.perToXer(Asn1Types.AdvisorySituationDataType, encodedMsgPerHex, HexPerData.unformatter, RawXerData.formatter);
                AdvisorySituationData asd = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(encodedMsgXerString);
                asdList.add(asd);
            } catch(CodecFailedException | FormattingFailedException | UnformattingFailedException | JAXBException e) {
                throw new RuntimeException(e);
            }
        }
	    
	    return asdList;
	}
	
	
	private List<AdvisorySituationBundle> createBundleList(List<AdvisorySituationData> asd, String dialogId) {
		List<AdvisorySituationDataDistribution> distributionList = createDistributionList(asd, dialogId);
		List<AdvisorySituationBundle> bundleList = new ArrayList<AdvisorySituationBundle>();
		
		for (AdvisorySituationDataDistribution distribution : distributionList) {
			AsdBundles bundles = distribution.getAsdBundles();
			bundleList.addAll(Arrays.asList(bundles.getAdvisorySituationBundle()));
		}

		
		return bundleList;
	} 
	
	
	private List<AdvisorySituationDataDistribution> createDistributionList(List<AdvisorySituationData> asd, String dialogId) {
		DialogID dialogIDObject = DialogID.fromValue("advSitDatDist");
		
		//dialogIDObject.setDialogId(DialogID.fromValue(dialogId));
		//dialogIDObject.setAdvSitDatDist("");
		
		AdvisorySituationDataDistributionList distributionListObject = MessageCreator.createAdvisorySituationDataDistributionList(asd, dialogIDObject, "00 00 00 00", "00 00 00 00");
		
		
		return distributionListObject.getDistributionList();
	}
	
}
