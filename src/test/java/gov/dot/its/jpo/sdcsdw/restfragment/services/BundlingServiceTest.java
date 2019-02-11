package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

public class BundlingServiceTest {
	private BundlingService bundlingService = new BundlingServiceImpl();
 
	@Test
	public void testBundles() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		Query query = new Query();

		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		for (int i = 0; i < 801; i++) {
			String rawXER = "<AdvisorySituationData><dialogID><advSitDataDep/></dialogID><seqID><data/></seqID><groupID>00000000</groupID><requestID>88D27197</requestID><timeToLive><week/></timeToLive><serviceRegion><nwCorner><lat>449984590</lat><long>-1110408170</long></nwCorner><seCorner><lat>411046740</lat><long>-1041113120</long></seCorner></serviceRegion><asdmDetails><asdmID>88D27197</asdmID><asdmType><tim/></asdmType><distType>10</distType><startTime><year>2017</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></startTime><stopTime><year>2018</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></stopTime><advisoryMessage>03805E001F5B70D07930EC9C236B00000000000F775D9B0301EA73E452D1539716C99E9D555100003F0BAD7580160307F82C5BF14005C00854E7C8A5A2A72E2D933D30579AAAA8B555508CE4539F22968A9CB8B64CF4C03F88600003E8F775D9B0</advisoryMessage></asdmDetails></AdvisorySituationData>";
			AdvisorySituationData advSitData = null;
			try {
				advSitData = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(rawXER);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			asdList.add(advSitData);
		}

		DialogID expectedDialog = new DialogID();
		expectedDialog.setAdvSitDatDist("");
		query.setDialogId(expectedDialog.getDialogId());
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.readTree(new JSONObject(mapper.writeValueAsString(asd)).toString()));
		}

		assertEquals(81, bundlingService.bundleOrDistribute(jsonList, "bundle", query.getDialogId()).size());
	}
	

	@Test
	public void testDistributions() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		Query query = new Query();

		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		for (int i = 0; i < 801; i++) {
			String rawXER = "<AdvisorySituationData><dialogID><advSitDataDep/></dialogID><seqID><data/></seqID><groupID>00000000</groupID><requestID>88D27197</requestID><timeToLive><week/></timeToLive><serviceRegion><nwCorner><lat>449984590</lat><long>-1110408170</long></nwCorner><seCorner><lat>411046740</lat><long>-1041113120</long></seCorner></serviceRegion><asdmDetails><asdmID>88D27197</asdmID><asdmType><tim/></asdmType><distType>10</distType><startTime><year>2017</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></startTime><stopTime><year>2018</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></stopTime><advisoryMessage>03805E001F5B70D07930EC9C236B00000000000F775D9B0301EA73E452D1539716C99E9D555100003F0BAD7580160307F82C5BF14005C00854E7C8A5A2A72E2D933D30579AAAA8B555508CE4539F22968A9CB8B64CF4C03F88600003E8F775D9B0</advisoryMessage></asdmDetails></AdvisorySituationData>";
			AdvisorySituationData advSitData = null;
			try {
				advSitData = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(rawXER);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			asdList.add(advSitData);
		}

		DialogID expectedDialog = new DialogID();
		expectedDialog.setAdvSitDatDist("");
		query.setDialogId(expectedDialog.getDialogId());
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.readTree(new JSONObject(mapper.writeValueAsString(asd)).toString()));
		}

		assertEquals(21, bundlingService.bundleOrDistribute(jsonList, "distribution", query.getDialogId()).size());
	}
	

	@Test
	public void testBundlesSingleEmptyASD() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		Query query = new Query();
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		for (int i = 0; i < 0; i++) {
			String rawXER = "<AdvisorySituationData><dialogID><advSitDataDep/></dialogID><seqID><data/></seqID><groupID>00000000</groupID><requestID>88D27197</requestID><timeToLive><week/></timeToLive><serviceRegion><nwCorner><lat>449984590</lat><long>-1110408170</long></nwCorner><seCorner><lat>411046740</lat><long>-1041113120</long></seCorner></serviceRegion><asdmDetails><asdmID>88D27197</asdmID><asdmType><tim/></asdmType><distType>10</distType><startTime><year>2017</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></startTime><stopTime><year>2018</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></stopTime><advisoryMessage>03805E001F5B70D07930EC9C236B00000000000F775D9B0301EA73E452D1539716C99E9D555100003F0BAD7580160307F82C5BF14005C00854E7C8A5A2A72E2D933D30579AAAA8B555508CE4539F22968A9CB8B64CF4C03F88600003E8F775D9B0</advisoryMessage></asdmDetails></AdvisorySituationData>";
			AdvisorySituationData advSitData = null;
			try {
				advSitData = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(rawXER);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			asdList.add(advSitData);
		}

		DialogID expectedDialog = new DialogID();
		expectedDialog.setAdvSitDatDist("");
		query.setDialogId(expectedDialog.getDialogId());
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.readTree(new JSONObject(mapper.writeValueAsString(asd)).toString()));
		}

		assertEquals(0, bundlingService.bundleOrDistribute(jsonList, "bundle", query.getDialogId()).size());
	}


	@Test
	public void testDistributionsSingleEmptyASD() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		Query query = new Query();
		
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		for (int i = 0; i < 0; i++) {
			String rawXER = "<AdvisorySituationData><dialogID><advSitDataDep/></dialogID><seqID><data/></seqID><groupID>00000000</groupID><requestID>88D27197</requestID><timeToLive><week/></timeToLive><serviceRegion><nwCorner><lat>449984590</lat><long>-1110408170</long></nwCorner><seCorner><lat>411046740</lat><long>-1041113120</long></seCorner></serviceRegion><asdmDetails><asdmID>88D27197</asdmID><asdmType><tim/></asdmType><distType>10</distType><startTime><year>2017</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></startTime><stopTime><year>2018</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></stopTime><advisoryMessage>03805E001F5B70D07930EC9C236B00000000000F775D9B0301EA73E452D1539716C99E9D555100003F0BAD7580160307F82C5BF14005C00854E7C8A5A2A72E2D933D30579AAAA8B555508CE4539F22968A9CB8B64CF4C03F88600003E8F775D9B0</advisoryMessage></asdmDetails></AdvisorySituationData>";
			AdvisorySituationData advSitData = null;
			try {
				advSitData = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(rawXER);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			asdList.add(advSitData);
		}

		DialogID expectedDialog = new DialogID();
		expectedDialog.setAdvSitDatDist("");
		query.setDialogId(expectedDialog.getDialogId());
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.readTree(new JSONObject(mapper.writeValueAsString(asd)).toString()));
		}

		assertEquals(1, bundlingService.bundleOrDistribute(jsonList, "distribution", query.getDialogId()).size());
	}
}
