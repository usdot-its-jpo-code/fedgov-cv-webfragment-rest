package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.DialogIDXml;


public class BundlingServiceTest {
	private BundlingService bundlingService = new BundlingServiceImpl();
	
	
	@Test
	public void testBundles() throws JsonGenerationException, JsonMappingException, IOException {
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();

		DialogIDXml expectedDialog = new DialogIDXml();
		expectedDialog.setAdvSitDatDist("");
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		
		for (int i = 0; i < 801; i++) {
		    InputStream is =  BundlingServiceTest.class.getResourceAsStream( "/sample-query-result.json");
		    JsonNode node = mapper.readTree(is);
        jsonList.add(node);
		}
		
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}
		
		assertEquals(81, bundlingService.bundleOrDistribute(jsonList, "bundle", expectedDialog.getDialogId()).size());
	}
	
 
	@Test
	public void testDistributions() throws JsonGenerationException, JsonMappingException, IOException {
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();

		DialogIDXml expectedDialog = new DialogIDXml();
		expectedDialog.setAdvSitDatDist("");
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		for (int i = 0; i < 801; i++) {
		    InputStream is =  BundlingServiceTest.class.getResourceAsStream( "/sample-query-result.json");
		    JsonNode node = mapper.readTree(is);
        jsonList.add(node);
		}
		
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		assertEquals(21, bundlingService.bundleOrDistribute(jsonList, "distribution", expectedDialog.getDialogId()).size());
	}
	

	@Test
	public void testBundlesSingleEmptyASD() throws JsonGenerationException, JsonMappingException, IOException {		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();

		DialogIDXml expectedDialog = new DialogIDXml();
		expectedDialog.setAdvSitDatDist("");
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		
		
		
		ObjectMapper mapper = new ObjectMapper();

		
		for (int i = 0; i < 0; i++) {
		    InputStream is =  BundlingServiceTest.class.getResourceAsStream( "/sample-query-result.json");
		    JsonNode node = mapper.readTree(is);
        jsonList.add(node);
		}
		
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		assertEquals(0, bundlingService.bundleOrDistribute(jsonList, "bundle", expectedDialog.getDialogId()).size());
	}


	@Test
	public void testDistributionsSingleEmptyASD() throws JsonGenerationException, JsonMappingException, IOException {		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();

		DialogIDXml expectedDialog = new DialogIDXml();
		expectedDialog.setAdvSitDatDist("");
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		
		
		
		ObjectMapper mapper = new ObjectMapper();

		
		for (int i = 0; i < 0; i++) {
		    InputStream is =  BundlingServiceTest.class.getResourceAsStream( "/sample-query-result.json");
		    JsonNode node = mapper.readTree(is);
		    jsonList.add(node);
		}
		
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		assertEquals(1, bundlingService.bundleOrDistribute(jsonList, "distribution", expectedDialog.getDialogId()).size());
	}
	
}
