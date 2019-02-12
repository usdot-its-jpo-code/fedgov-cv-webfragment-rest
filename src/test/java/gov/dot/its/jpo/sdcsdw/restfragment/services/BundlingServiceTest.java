package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.Models.DialogIdType;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.Base64PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataFormatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.XerDataUnformatter;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

public class BundlingServiceTest {
	private BundlingService bundlingService = new BundlingServiceImpl();
	
	public static String createSelfClosingTags(String xerToModify) {
		return xerToModify.replaceAll("<([^>]*)><\\/\\1>", "<$1\\/>");
	}
	
	@Test
	public void serializeDeserializeTest() throws UnformattingFailedException, CodecFailedException, FormattingFailedException {
        XerDataUnformatter<String, RawXerData> xerUnformatter = RawXerData.unformatter;
        PerDataFormatter<String, PerData<String>> perFormatter = Base64PerData::new;
        
		String rawXER = "<AdvisorySituationData><dialogID><advSitDataDep/></dialogID><seqID><data/></seqID><groupID>00000000</groupID><requestID>88D27197</requestID><timeToLive><week/></timeToLive><serviceRegion><nwCorner><lat>449984590</lat><long>-1110408170</long></nwCorner><seCorner><lat>411046740</lat><long>-1041113120</long></seCorner></serviceRegion><asdmDetails><asdmID>88D27197</asdmID><asdmType><tim/></asdmType><distType>10</distType><startTime><year>2017</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></startTime><stopTime><year>2018</year><month>12</month><day>1</day><hour>17</hour><minute>47</minute></stopTime><advisoryMessage>03805E001F5B70D07930EC9C236B00000000000F775D9B0301EA73E452D1539716C99E9D555100003F0BAD7580160307F82C5BF14005C00854E7C8A5A2A72E2D933D30579AAAA8B555508CE4539F22968A9CB8B64CF4C03F88600003E8F775D9B0</advisoryMessage></asdmDetails></AdvisorySituationData>";
		AdvisorySituationData advSitData = null;
		String deserialize = null;
	
		//XER to POJO
		try {
			advSitData = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(rawXER);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		
		//POJO to XER
		try {
			deserialize = createSelfClosingTags(XerJaxbCodec.JaxbPojoToXer(advSitData));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		
		
		//XER to PER
		PerXerCodec.xerToPer(Asn1Types.AdvisorySituationDataType, deserialize, xerUnformatter, perFormatter);
		
	}
	
	@Test
	public void testBundles() throws JsonGenerationException, JsonMappingException, IOException {
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
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		//assertEquals(81, bundlingService.bundleOrDistribute(jsonList, "bundle", expectedDialog.getDialogIdType().getValue()).size());
	}
	
 
	@Test
	public void testDistributions() throws JsonGenerationException, JsonMappingException, IOException {
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
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		//assertEquals(21, bundlingService.bundleOrDistribute(jsonList, "distribution", expectedDialog.getDialogIdType().getValue()).size());
	}
	

	@Test
	public void testBundlesSingleEmptyASD() throws JsonGenerationException, JsonMappingException, IOException {		
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
		//expectedDialog.setDialogId(DialogIdType.ADV_SIT_DAT_DIST);
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		//assertEquals(0, bundlingService.bundleOrDistribute(jsonList, "bundle", expectedDialog.getDialogIdType().getValue()).size());
	}


	@Test
	public void testDistributionsSingleEmptyASD() throws JsonGenerationException, JsonMappingException, IOException {		
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
		//expectedDialog.setDialogId(DialogIdType.ADV_SIT_DAT_DIST);
		
		List<JsonNode> jsonList = new ArrayList<JsonNode>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (AdvisorySituationData asd : asdList) {
			jsonList.add(mapper.valueToTree(asd));
		}

		//assertEquals(1, bundlingService.bundleOrDistribute(jsonList, "distribution", expectedDialog.getDialogIdType().getValue()).size());
	}
}
