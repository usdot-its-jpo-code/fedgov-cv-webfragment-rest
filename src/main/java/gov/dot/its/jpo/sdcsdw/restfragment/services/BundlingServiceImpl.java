package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;


import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistributionList;
import gov.dot.its.jpo.sdcsdw.Models.AsdBundles;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.udpdialoghandler.service.MessageCreator;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;





public class BundlingServiceImpl implements BundlingService {
	@Override
	public List<JsonNode> bundleOrDistribute(List<JsonNode> jsonList, String packageType, String dialogId) throws JsonParseException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		List<JsonNode> returnJsons = new ArrayList<JsonNode>();
		
		for (JsonNode json : jsonList) {
			AdvisorySituationData asd = mapper.readValue(json.toString(), AdvisorySituationData.class);
			asdList.add(asd);
		}
		
		if (packageType.equals("bundle")) {
			List<AdvisorySituationBundle> bundleList = createBundleList(asdList, dialogId);
			
			for (AdvisorySituationBundle bundle : bundleList) {		
				JSONObject json = new JSONObject(mapper.writeValueAsString(bundle));
				returnJsons.add(mapper.readTree(json.toString()));
			}
		} else {
			List<AdvisorySituationDataDistribution> distributionList = createDistributionList(asdList, dialogId);
			
			for (AdvisorySituationDataDistribution distribution : distributionList) {
				JSONObject json = new JSONObject(mapper.writeValueAsString(distribution));
				returnJsons.add(mapper.readTree(json.toString()));
			}
		}
		
		return returnJsons;
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
		DialogID dialogIDObject = new DialogID();
		
		if (dialogId.equals("vehSitData")) {
			dialogIDObject.setVehSitData("");
		}
		else if (dialogId.equals("dataSubsription")) {
			dialogIDObject.setDataSubscription("");
		}
		else if (dialogId.equals("advSitDataDep")) {
			dialogIDObject.setAdvSitDatDep("");
		}
		else if (dialogId.equals("advSitDataDist")) {
			dialogIDObject.setAdvSitDatDist("");
		}
		else if (dialogId.equals("reserved1")) {
			dialogIDObject.setReserved1("");
		}
		else if (dialogId.equals("reserved2")) {
			dialogIDObject.setReserved2("");
		}
		else if (dialogId.equals("objReg")) {
			dialogIDObject.setObjReg("");
		}
		else if (dialogId.equals("objDisc")) {
			dialogIDObject.setObjDisc("");
		}
		else if (dialogId.equals("intersectionSitDataDep")) {
			dialogIDObject.setIntersectionSitDataDep("");
		}
		else if (dialogId.equals("intersectionSitDataQuery")) {
			dialogIDObject.setIntersectionSitDataQuery("");
		}
		
		AdvisorySituationDataDistributionList distributionListObject = MessageCreator.createAdvisorySituationDataDistributionList(asd, dialogIDObject, "00 00 00 00", "00 00 00 00");
		
		
		return distributionListObject.getDistributionList();
	}
	
}
