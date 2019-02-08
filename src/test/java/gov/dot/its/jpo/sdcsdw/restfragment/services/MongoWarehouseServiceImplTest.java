package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;

public class MongoWarehouseServiceImplTest {

	private String fullResult = "{ \"_id\" : { \"$oid\" : \"5c0ec1f7d3630db79789ff0a\"} , \"systemDepositName\" : \"SDW 2.3\" , \"encodeType\" : \"HEX\" , \"encodedMsg\" : \"C44000000001805300018053000A27A318401C9C37FF89D5B340073A20CFE0C029800201204070103FC003F01F45046270AC8AA888A860600008000000003E802002CC949E9CF0C4CBB77D6CBD2372C3BB5ECD3B84EE060CA39CC3BA94E21FFFE000040100174CD2C310000000A326DC05AA002800000000000000000040416029105D1D195B9D1A5BDB8B88151A1A5CC81A5CC8185B88115B595C99D95B98DE48105B195C9D0B88151A195C99481A5CC8184819D85CC81B19585AC81C995C1BDC9D195908185A195859081BDB88119C985B9ADB1A5B8814DD1C99595D080B88119BDC881E5BDD5C881CD859995D1E4B081D1D5C9B881C9A59DA1D081BDB88149A5BDC195B1B194814DD1C99595D081D1BC8185D9BDA59081D1A19481A5B98DA59195B9D0B814000\" , \"dialogId\" : 156 , \"sequenceId\" : 5 , \"groupId\" : 0 , \"requestId\" : 402993152 , \"recordId\" : 402993152 , \"timeToLive\" : 5 , \"nwLat\" : 43 , \"nwLon\" : -84 , \"seLat\" : 42 , \"seLon\" : -83 , \"asdmId\" : 402993152 , \"asdmType\" : 2 , \"distType\" : 1 , \"advisoryMessage\" : \"038081FE001F80FA2823138564554445430300004000000001F401001664A4F4E786265DBBEB65E91B961DDAF669DC277030651CE61DD4A710FFFF0000200800BA669618800000051936E02D500140000000000000000002020B014882E8E8CADCE8D2DEDC5C40A8D0D2E640D2E640C2DC408ADACAE4CECADCC6F24082D8CAE4E85C40A8D0CAE4CA40D2E640C240CEC2E640D8CAC2D640E4CAE0DEE4E8CAC840C2D0CAC2C840DEDC408CE4C2DCD6D8D2DC40A6E8E4CACAE8405C408CDEE440F2DEEAE440E6C2CCCAE8F25840E8EAE4DC40E4D2CED0E840DEDC40A4D2DEE0CAD8D8CA40A6E8E4CACAE840E8DE40C2ECDED2C840E8D0CA40D2DCC6D2C8CADCE85C0A00\" , \"region\" : { \"type\" : \"Polygon\" , \"coordinates\" : [ [ [ -84 , 43] , [ -84 , 42] , [ -83 , 42] , [ -83 , 43] , [ -84 , 43]]]} , \"createdAt\" : { \"$date\" : \"2018-12-10T19:43:51.586Z\"} , \"expireAt\" : { \"$date\" : \"2019-12-10T19:43:51.586Z\"}}";
	private String base64Result = "C44000000001805300018053000A27A318401C9C37FF89D5B340073A20CFE0C029800201204070103FC003F01F45046270AC8AA888A860600008000000003E802002CC949E9CF0C4CBB77D6CBD2372C3BB5ECD3B84EE060CA39CC3BA94E21FFFE000040100174CD2C310000000A326DC05AA002800000000000000000040416029105D1D195B9D1A5BDB8B88151A1A5CC81A5CC8185B88115B595C99D95B98DE48105B195C9D0B88151A195C99481A5CC8184819D85CC81B19585AC81C995C1BDC9D195908185A195859081BDB88119C985B9ADB1A5B8814DD1C99595D080B88119BDC881E5BDD5C881CD859995D1E4B081D1D5C9B881C9A59DA1D081BDB88149A5BDC195B1B194814DD1C99595D081D1BC8185D9BDA59081D1A19481A5B98DA59195B9D0B814000";
	private String hexResult = "0b8e34d34d34d34d35f34e77d34d35f34e77d34d00dbb037d7ce34d42f42dfb145f3d0f9077e34d3bdc0db40851340b4dbdf34d36d35db4e34ef4d74dc50b4d37174d45e39d38eb6ef4002f0003cf3c03ceb4eb4d34d3cd34d34d34d34dc4f34db4d36082f78f44f421740b808107bec3e82043db7ef60b7041e44083dc1f38104d3ad02037f420b7040f78136d45145134d34d38d35d34d7be020f60b7d74d34d34d34037dba0c2d39000d34dbcd34d34d34d34d34d34d34d34d34e34e35eb4dbdd74e43d43d7de41f43d40e410c1f01f3cd79d40d40e420bcd40e420bcd7ce41f3cd75e41e7de42f7d0fde41f7c0c4e3cd74e41d7de42f43d01f3cd79d40d7de42f7de3cd40e420bcd7ce3cd7d0fce420bcd41d7de7ce400bcd42f7de42d410c2f43d7de7dd3cd7ce40d7de7ce7dd3cd410c1f3cd75f42f7ce41f400c1d40e41f3cd780c3d42f7de7de43d3cd01f3cd75f410c2f3cd44e410c3e42f3cd420fce7df7de43d44e01d3cd43d43e42f41f3cd42f40e7d0c0d43d3cd410c1f3cd78f40e410c2d7de41d41d7de3cd780c3d42f7de7de43d3cd43d410bcd7ce43f410c0e7dd3cd43d40d7de3cd40e41f7c0c0e7dd7de41f43d01f35e34d34";
	private HashMap<String, Object> fullAsJson;
	private MongoWarehouseServiceImpl mockService = new MongoWarehouseServiceImpl(null);
	private BasicDBObject dbobj;
	private List<DBObject> dbobjList;
	private Query query = new Query();
	
	@Before
	public void setup() {
		try {
			fullAsJson = new ObjectMapper().readValue(fullResult, HashMap.class);
			dbobj = new BasicDBObject();
			dbobj.putAll(fullAsJson);
			dbobjList = new ArrayList<DBObject>();
			dbobjList.add(dbobj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Full result encoding
	@Test
	public void testEncodeResults1() {
		query.setResultEncoding("full");
		try {
			List<String> encodedRecords = mockService.encodeRecords(dbobjList, query);
			HashMap<String, Object> encodedAsJson = new ObjectMapper().readValue(encodedRecords.get(0), HashMap.class);
			assertEquals(fullAsJson, encodedAsJson);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//base64 result encoding
	@Test
	public void testEncodeResults2() {
		query.setResultEncoding("base64");
		try {
			List<String> encodedRecords = mockService.encodeRecords(dbobjList, query);
			assertEquals(base64Result, encodedRecords.get(0));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEncodeResults3() {
		query.setResultEncoding("hex");
		try {
			List<String> encodedRecords = mockService.encodeRecords(dbobjList, query);
			assertEquals(hexResult, encodedRecords.get(0));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
