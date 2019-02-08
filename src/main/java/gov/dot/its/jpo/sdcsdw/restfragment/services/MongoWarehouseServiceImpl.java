package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import gov.dot.its.jpo.sdcsdw.restfragment.config.MongoClientConnection;
import gov.dot.its.jpo.sdcsdw.restfragment.config.MongoClientLookup;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.util.QueryOptions;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

@Service
@Primary
public class MongoWarehouseServiceImpl implements WarehouseService {

	private MongoClientLookup mongoClientLookup;
	private static final Logger logger = LoggerFactory.getLogger(MongoWarehouseServiceImpl.class);
	
	//Required index names which must match index names on each MongoDB collection
	private static final String NO_SORT_INDEX_NAME = "region_2dsphere_createdAt_1";
    private static final String CREATED_AT_SORT_INDEX_NAME = "createdAt_1";
    private static final String REQUEST_ID_SORT_INDEX_NAME = "requestId_1_createdAt_1";
    
    @Autowired
    public MongoWarehouseServiceImpl(MongoClientLookup mongoClientLookup) {
    	this.mongoClientLookup = mongoClientLookup;
    }
	
	@Override
	public List<String> executeQuery(Query query) throws InvalidQueryException {				
		List<String> encodedRecords = null;
		
		//Use mongoDBConnection to execute the query
		//Check that system name is valid
		checkValidSystemName(query);
		BasicDBObject mongoQuery = buildMongoQuery(query);
		DBCursor cursor = buildCursor(mongoQuery, query);
		List<DBObject> retrievedRecords = retrieveRecords(cursor);
		encodedRecords = encodeRecords(retrievedRecords, query);

		
		return encodedRecords;
	}
	
	private void checkValidSystemName(Query query) throws InvalidQueryException {
		String systemName = query.getSystemQueryName();
		if(mongoClientLookup.lookupMongoClient(systemName) == null)
			throw new InvalidQueryException("Invalid system name provided: " + query.getSystemQueryName());
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
				//Set start date operator. Default is GTE.
				String startDateOperator = query.getStartDateOperator();
				
				dateRange.put("$" + startDateOperator.toLowerCase(), startDate);
			}
			if (endDate != null) {
				//Set end date operator. Default is LTE.
				String endDateOperator = query.getEndDateOperator();
				
				dateRange.put("$" + endDateOperator.toLowerCase(), endDate);
			}
			
			mongoQuery.append("createdAt", dateRange);
		}
		
		return mongoQuery;
	}
	
	private DBCursor buildCursor(BasicDBObject mongoQuery, Query query) {
		StringBuilder queryParams = new StringBuilder();
		DBObject fieldNames = null;
		
		//Set result encoding. Default is hex.
		String resultEncoding = query.getResultEncoding();
		
		if (!resultEncoding.equals("full")) {
			fieldNames = new BasicDBObject(2);
			fieldNames.put("encodedMsg", 1);
			fieldNames.put("_id", 0);
			queryParams.append(" fieldNames: ").append(fieldNames);
		}
		
		//Get MongoClientConnection from MongoClientLookup
		MongoClientConnection mongo = mongoClientLookup.lookupMongoClient(query.getSystemQueryName());
		String collectionName = mongo.getConfig().collectionName;
		DBCollection collection = mongo.getDatabase().getCollection(collectionName);
		DBCursor cursor = (fieldNames == null) ? collection.find(mongoQuery) : collection.find(mongoQuery, fieldNames);
		
		//If order by field not provided, defaults to none (i.e., no ordering)
		if (!query.getOrderByField().equals("none")) {
			String orderByField = query.getOrderByField();
			
			//Order by order defaults to 1 (ascending). If descending, set appropriately.
			int orderByOrder = 1;
			if(query.getOrderByOrder().equals("descending") || query.getOrderByOrder().equals("-1"))
				orderByOrder = -1;
			
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
		if(query.getSkip() > 0) {
			cursor.skip(query.getSkip().intValue());
			queryParams.append(" skip: ").append(query.getSkip());
		}
		
		//If limit is provided and greater than 0, use limit. Otherwise ignored (i.e., limit is 0)
		if(query.getLimit() > 0) {
			cursor.limit(query.getLimit().intValue());
			queryParams.append(" limit: ").append(query.getLimit());
		}
		
		logger.info("Cursor built for query on: " + mongo.getConfig().systemName + " " + 
					collectionName + " : " + mongoQuery + " , " + queryParams);
		
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
	
	public List<String> encodeRecords(List<DBObject> dbObjs, Query query) {
		
		//The list for encoded records. All records in the list will have the same encoding based on the
		//query's specified result encoding. This is either full, base64, or hex.
		List<String> encodedRecords = new ArrayList<String>();
		
		//Get the result encoding. Default is hex.
		String resultEncoding = query.getResultEncoding();
		
		//For each dbObject returned by the query
		for(DBObject dbObj : dbObjs) {
			String record = null;
			
			//If the object has the encodedMsg field
			if(dbObj.containsField("encodedMsg")) {
				if (resultEncoding.equalsIgnoreCase("full")) {
					//Full encoding returns the full object
					record = dbObj.toString();
				} else if (resultEncoding.equalsIgnoreCase("base64")) {
					//Base64 encoding returns the encoded message
					record = dbObj.get("encodedMsg").toString();
				} else if (resultEncoding.equalsIgnoreCase("hex")) {
					//Hex encoding decodes the base64 encoded message, and then encodes it into hex
					record = Hex.encodeHexString(Base64.decodeBase64(dbObj.get("encodedMsg").toString()));
				}
				
				if(record != null)
					encodedRecords.add(record);
				
			} else {
				logger.error("Missing field encodedMsg for message " + dbObj);
			}
		}
		
		return encodedRecords;
	}
}
