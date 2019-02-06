package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.RawPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.restfragment.config.MongoDBConnection;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.util.QueryOptions;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

@Service
@Primary
public class MongoWarehouseServiceImpl implements WarehouseService {

	private MongoDBConnection mongo;
	
	//Required index names which must match index names on each MongoDB collection
	private static final String NO_SORT_INDEX_NAME = "region_2dsphere_createdAt_1";
    private static final String CREATED_AT_SORT_INDEX_NAME = "createdAt_1";
    private static final String REQUEST_ID_SORT_INDEX_NAME = "requestId_1_createdAt_1";
	
	@Override
	public List<AdvisorySituationData> executeQuery(Query query) {		
		List<AdvisorySituationData> asdList = null;
		
		//Use mongoDBConnection to execute the query
		try {
			BasicDBObject mongoQuery = buildMongoQuery(query);
			DBCursor cursor = buildCursor(mongoQuery, query);
			List<DBObject> retrievedRecords = retrieveRecords(cursor);
			asdList = createListOfASDS(retrievedRecords, query);
		} catch (InvalidQueryException e) {
			//log the exception
		}
		
		return asdList;
	}
	
	private BasicDBObject buildMongoQuery(Query query) throws InvalidQueryException {
		BasicDBObject mongoQuery = new BasicDBObject();
		
		//If all coordinates are provided (i.e., none are equal to null), use coordinates
		if(query.getNwLat() != null && query.getNwLon() != null && query.getSeLat() != null && query.getSeLon() != null) {
			
			double nwLat = query.getNwLat();
			double nwLon = query.getNwLon();
			double seLat = query.getSeLat();
			double seLon = query.getSeLon();
			
			double neLat = nwLat;
			double neLon = seLon;
			double swLat = seLat;
			double swLon = nwLon;
			
			List<double[]> coordinates = new ArrayList<double[]>();
			coordinates.add(new double[] { nwLon, nwLat });
			coordinates.add(new double[] { neLon, neLat });
			coordinates.add(new double[] { seLon, seLat });
			coordinates.add(new double[] { swLon, swLat});
			coordinates.add(new double[] { nwLon, nwLat });
			// have to wrap the coords in another list to get the triple "[[[" that mongo wants
			List<List<double[]>> coordinatesList = new ArrayList<List<double[]>>();
			coordinatesList.add(coordinates);
			
			DBObject geoWithin = BasicDBObjectBuilder.start()
					.push("$geoIntersects")
						.push("$geometry")
					    	.add("type", "Polygon")
					        .add("coordinates", coordinatesList).get();
			mongoQuery.append("region", geoWithin);
		}
		
		//If either start date or end date are provided
		if(query.getStartDate() != null || query.getEndDate() != null) {
			
			Date startDate = null;
			Date endDate = null;
				
			if(query.getStartDate() != null) {
				try {
					startDate = QueryOptions.getSDFNoMillis().parse(query.getStartDate());
				} catch (ParseException e) {
					try {
						QueryOptions.getSDFMillis().parse(query.getStartDate());
					} catch (ParseException e2) {
						throw new InvalidQueryException("Invalid startDate: " + query.getStartDate() + 
							", must match format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS");
					}
				}
			}
			
			if(query.getEndDate() != null) {
				try {
					endDate = QueryOptions.getSDFNoMillis().parse(query.getEndDate());
				} catch (ParseException e) {
					try {
						QueryOptions.getSDFMillis().parse(query.getEndDate());
					} catch (ParseException e2) {
						throw new InvalidQueryException("Invalid endDate: " + query.getEndDate() + 
							", must match format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS");
					}
				}
			}
			
			DBObject dateRange = new BasicDBObject();
			if (startDate != null) {
				//Start date operator defaults to GTE. If provided, set appropriately.
				String startDateOperator = "GTE";
				if(query.getStartDateOperator() != null)
					startDateOperator = query.getStartDateOperator();
				
				dateRange.put("$" + startDateOperator.toLowerCase(), startDate);
			}
			if (endDate != null) {
				//End date operator defaults to LTE. If provided, set appropriately.
				String endDateOperator = "LTE";
				if(query.getEndDateOperator() != null)
					endDateOperator = query.getEndDateOperator();
				
				dateRange.put("$" + endDateOperator.toLowerCase(), endDate);
			}
			
			mongoQuery.append("createdAt", dateRange);
		}
		
		return mongoQuery;
	}
	
	private DBCursor buildCursor(BasicDBObject mongoQuery, Query query) throws InvalidQueryException {
		StringBuilder queryParams = new StringBuilder();
		DBObject fieldNames = null;
		
		//Result encoding defaults to hex. If provided, set appropriately.
		String resultEncoding = "hex";
		if (query.getResultEncoding() != null)
			resultEncoding = query.getResultEncoding();
		
		if (!resultEncoding.equals("full")) {
			fieldNames = new BasicDBObject(2);
			fieldNames.put("encodedMsg", 1);
			fieldNames.put("_id", 0);
			queryParams.append(" fieldNames: ").append(fieldNames);
		}
		
		String collectionName = mongo.getConfig().collectionName;
		DBCollection collection = mongo.getDatabase().getCollection(collectionName);
		DBCursor cursor = (fieldNames == null) ? collection.find(mongoQuery) : collection.find(mongoQuery, fieldNames);
		
		//If order by field not provided, defaults to none (i.e., no ordering)
		if (query.getOrderByField() != null && !query.getOrderByField().equals("none")) {
			String orderByField = query.getOrderByField();
			
			//Order by order defaults to 1 (ascending). If provided, set appropriately.
			int orderByOrder = 1;
			if(query.getOrderByOrder() != null) {
				if(query.getOrderByOrder().equals("descending") || query.getOrderByOrder().equals("-1"))
					orderByOrder = -1;
			}
			BasicDBObject orderBy = new BasicDBObject(orderByField, orderByOrder);
			queryParams.append(" orderBy: ").append(orderBy);
			cursor.sort(orderBy);
			
			if (orderByField.equals("createdAt"))
				cursor.hint(CREATED_AT_SORT_INDEX_NAME);
			else if (orderByField.equals("requestId"))
				cursor.hint(REQUEST_ID_SORT_INDEX_NAME);
		} else {
			cursor.hint(NO_SORT_INDEX_NAME);
		}
		
		//If skip is provided and greater than 0, use skip. Otherwise ignored (i.e., skip is 0)
		if(query.getSkip() != null && query.getSkip() > 0) {
			cursor.skip(query.getSkip());
			queryParams.append(" skip: ").append(query.getSkip());
		}
		
		//If limit is provided and greater than 0, use limit. Otherwise ignored (i.e., limit is 0)
		if(query.getLimit() != null && query.getLimit() > 0) {
			cursor.limit(query.getLimit());
			queryParams.append(" limit: ").append(query.getLimit());
		}
		
		return cursor;
	}
	
	private List<DBObject> retrieveRecords(DBCursor cursor) {
		List<DBObject> result = new ArrayList<DBObject>();
		try {
			while (cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				result.add(dbObj);
			}
		} finally {
			cursor.close();
		}
		
		return result;
	}
	
	//Mirrors method used in UDP interface
	private List<AdvisorySituationData> createListOfASDS(List<DBObject> retrievedRecords, Query query) {
		List<AdvisorySituationData> asdList = new ArrayList<AdvisorySituationData>();
		
//		String resultEncoding = "hex";
//		if(query.getResultEncoding() != null)
//			resultEncoding = query.getResultEncoding();

		for(DBObject dbObj : retrievedRecords) {
			String hexEncodedASD = dbObj.get("encodedMsg").toString();
			byte[] hexEncodedASDAsByte = null;
			try {
				hexEncodedASDAsByte = Hex.decodeHex(hexEncodedASD);
			} catch (DecoderException e) {
				continue;
			}
			
			try {
				//Decode byte array to XML
				String xerEncodedASD = (PerXerCodec.perToXer(Asn1Types.getAsn1TypeByName("AdvisorySituationData"),
						hexEncodedASDAsByte, RawPerData.unformatter, RawXerData.formatter));
				
				//Convert XML to POJO
				AdvisorySituationData asdObject = (AdvisorySituationData) XerJaxbCodec.XerToJaxbPojo(xerEncodedASD);
				asdList.add(asdObject);
			} catch (CodecFailedException | FormattingFailedException | UnformattingFailedException | JAXBException e) {
				// Going to ignore this message
				//logger.error("Failed to decode and convert to POJO ASD retrieved from Mongo", e);
			}
		}
		
		return asdList;
	}
}
