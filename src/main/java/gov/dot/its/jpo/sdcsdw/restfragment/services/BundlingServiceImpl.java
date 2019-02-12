package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistributionList;
import gov.dot.its.jpo.sdcsdw.Models.AsdBundles;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.Models.DialogIdType;
import gov.dot.its.jpo.sdcsdw.udpdialoghandler.service.MessageCreator;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;




@Service
public class BundlingServiceImpl implements BundlingService {
	@Override
	public List<JsonNode> bundleOrDistribute(List<JsonNode> jsonList, String packageType, String dialogId) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		List<JsonNode> returnJsons = new ArrayList<JsonNode>();
		
		for (JsonNode json : jsonList) {
			//To be changed, currently implementing alternative to this is encoding service
			AdvisorySituationData asd = mapper.treeToValue(json, AdvisorySituationData.class);
			
			asdList.add(asd);
		}
		
		if (packageType.equals("bundle")) {
			List<AdvisorySituationBundle> bundleList = createBundleList(asdList, dialogId);
			
			for (AdvisorySituationBundle bundle : bundleList) {
				returnJsons.add(mapper.valueToTree(bundle));
			}
		} else {
			List<AdvisorySituationDataDistribution> distributionList = createDistributionList(asdList, dialogId);
			
			for (AdvisorySituationDataDistribution distribution : distributionList) {
				returnJsons.add(mapper.valueToTree(distribution));
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
		
		dialogIDObject.setDialogId(DialogIdType.fromValue(dialogId));
		
		AdvisorySituationDataDistributionList distributionListObject = MessageCreator.createAdvisorySituationDataDistributionList(asd, dialogIDObject, "00 00 00 00", "00 00 00 00");
		
		
		return distributionListObject.getDistributionList();
	}
	
}
