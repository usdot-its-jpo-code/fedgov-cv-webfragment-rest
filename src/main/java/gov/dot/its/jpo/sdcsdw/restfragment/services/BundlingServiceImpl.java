package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistributionList;
import gov.dot.its.jpo.sdcsdw.Models.AsdBundles;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.udpdialoghandler.service.MessageCreator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class BundlingServiceImpl implements BundlingService {
	@Override
	public List<JSONObject> bundleOrDistribute(List<JSONObject> jsonList, String packageType, String dialogId) throws JsonParseException, JsonMappingException, IOException, ParseException  {
		ObjectMapper mapper = new ObjectMapper();
		JSONParser parser = new JSONParser();
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		List<JSONObject> returnJsons = new ArrayList<JSONObject>();
		
		for (JSONObject json : jsonList) {
			AdvisorySituationData asd = mapper.readValue(json.toString(), AdvisorySituationData.class);
			asdList.add(asd);
		}
		
		if (packageType.equals("bundle")) {
			List<AdvisorySituationBundle> bundleList = createBundleList(asdList, dialogId);
			
			for (AdvisorySituationBundle bundle : bundleList) {		
				JSONObject json = (JSONObject) parser.parse(mapper.writeValueAsString(bundle));
				returnJsons.add(json);
			}
		} else {
			
		}
		
		return returnJsons;
	}
	
	
	@Override
	public List<AdvisorySituationBundle> createBundleList(List<AdvisorySituationData> asd, String dialogId) {
		List<AdvisorySituationDataDistribution> distributionList = createDistributionList(asd, dialogId);
		List<AdvisorySituationBundle> bundleList = new ArrayList<AdvisorySituationBundle>();
		
		for (AdvisorySituationDataDistribution distribution : distributionList) {
			AsdBundles bundles = distribution.getAsdBundles();
			bundleList.addAll(Arrays.asList(bundles.getAdvisorySituationBundle()));
		}

		
		return bundleList;
	} 
	
	
	@Override
	public List<AdvisorySituationDataDistribution> createDistributionList(List<AdvisorySituationData> asd, String dialogId) {
		DialogID dialogIDObject = new DialogID();
		dialogIDObject.setAdvSitDatDist("");
		AdvisorySituationDataDistributionList distributionListObject = MessageCreator.createAdvisorySituationDataDistributionList(asd, dialogIDObject, "00 00 00 00", "00 00 00 00");
		
		
		return distributionListObject.getDistributionList();
	}
	
}
