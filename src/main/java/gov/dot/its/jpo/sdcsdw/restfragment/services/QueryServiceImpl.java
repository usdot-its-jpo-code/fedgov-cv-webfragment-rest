package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.text.ParseException;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.util.QueryOptions;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

@Service
@Primary
public class QueryServiceImpl implements QueryService {

	private WarehouseService warehouseService;
	private static final Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);
	
	@Autowired
	public QueryServiceImpl(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	
	@Override
	public void setDefaults(Query query) {
		if(query.getResultEncoding() == null)
			query.setResultEncoding(QueryOptions.getDefaultResultEncoding());
		
		if(query.getResultPackaging() == null)
			query.setResultPackaging(QueryOptions.getDefaultResultPackaging());
		
		if(query.getOrderByField() == null)
			query.setOrderByField(QueryOptions.getDefaultOrderByField());
		
		if(query.getOrderByOrder() == null)
			query.setOrderByOrder(QueryOptions.getDefaultOrderByOrder());
		
		if(query.getSkip() == null)
			query.setSkip(QueryOptions.getDefaultSkip());
		
		if(query.getLimit() == null)
			query.setLimit(QueryOptions.getDefaultLimit());
		
		if(query.getStartDateOperator() == null)
			query.setStartDateOperator(QueryOptions.getDefaultStartDateOperator());
		
		if(query.getEndDateOperator() == null)
			query.setEndDateOperator(QueryOptions.getDefaultEndDateOperator());
	}

	@Override
	public void validateQuery(Query query) throws InvalidQueryException {
		
		try {
			
			//REQUIRED: systemQueryName
			//Check if there is a value for systemQueryName
			if(query.getSystemQueryName() == null)
				throw new InvalidQueryException("Missing required parameter systemQueryName: " + query.getSystemQueryName());
			
			//REQUIRED: dialogId
			//Check if there is a value for dialogId
			if(query.getDialogId() != null) {
				
				//Check if the value for dialogId is a valid option
				if(!QueryOptions.getDialogIdOptions().contains(query.getDialogId())) {
					
					throw new InvalidQueryException("Inavlid dialogID: " + query.getDialogId() +
							", not one of the supported dialogIDs: " + QueryOptions.getDialogIdOptions().toString());
				}
			
			} else {
				throw new InvalidQueryException("Missing dialogID in message: " + query.toString());
			}
			
			//OPTIONAL: startDate
			//If there is a start date
			if(query.getStartDate() != null) {
				
				//Try parsing into first format
				try {
					QueryOptions.getSDFNoMillis().parse(query.getStartDate());
				} catch (ParseException e) {
					//If first format invalid, try parsing into second format
					try {
						QueryOptions.getSDFMillis().parse(query.getStartDate());
					} catch (ParseException e2) {
						throw new InvalidQueryException("Invalid startDate: " + query.getStartDate() + 
								", must match format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS");
					}
				}
			}
			
			//OPTIONAL: startDateOperator
			//If there is a start date operator
			if(query.getStartDateOperator() != null) {
				
				//Check if the start date operator is valid
				if(!QueryOptions.getStartDateOperatorOptions().contains(query.getStartDateOperator())) {
					throw new InvalidQueryException("Invalid startDateOperator: " + query.getStartDateOperator() + 
							", not one of the supported startDateOperator: " + QueryOptions.getStartDateOperatorOptions().toString());
				}
			}
			
			//OPTIONAL: endDate
			//If there is an end date
			if(query.getEndDate() != null) {
				try {
					QueryOptions.getSDFNoMillis().parse(query.getEndDate());
				} catch (ParseException e) {
					try {
						QueryOptions.getSDFMillis().parse(query.getEndDate());
					} catch (ParseException e2) {
						throw new InvalidQueryException("Invalid endDate: " + query.getEndDate() + 
								", must match format yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS");
					} 
				}
			}
			
			//OPTIONAL: endDateOperator
			//If there is an end date operator
			if(query.getEndDateOperator() != null) {
				
				//Check if the end date operator is valid
				if (!QueryOptions.getEndDateOperatorOptions().contains(query.getEndDateOperator())) {
					throw new InvalidQueryException("Invalid endDateOperator: " + query.getEndDateOperator() + 
							", not one of the supported endDateOperator: " + QueryOptions.getEndDateOperatorOptions().toString());
				}
			}
			
			//OPTIONAL: coordinates
			//If any one coordinate is provided (i.e., not null), all coordinates must be provided.
			if(query.getNwLat() != null || query.getNwLon() != null || query.getSeLat() != null || query.getSeLon() != null) {
				if(query.getNwLat() == null)
					throw new InvalidQueryException("The field nwLat is required");
				if(query.getNwLon() == null)
					throw new InvalidQueryException("The field nwLon is required");
				if(query.getSeLat() == null)
					throw new InvalidQueryException("The field seLat is required");
				if(query.getSeLon() == null)
					throw new InvalidQueryException("The field seLon is required");
			}
			
			//OPTIONAL: orderByOrder
			//If a value is provided
			if(query.getOrderByOrder() != null) {
				
				//Check if the orderByOrder is valid
				if(!QueryOptions.getOrderByOrderOptions().contains(query.getOrderByOrder())) {
					throw new InvalidQueryException("Invalid orderByOrder: " + query.getOrderByOrder() + 
							", must be one of: " + QueryOptions.getOrderByOrderOptions().toString());
				}
			}
			
			//OPTIONAL: orderByField
			//If a value is provided
			if(query.getOrderByField() != null) {
				
				//Check if the orderByField is valid
				if(!QueryOptions.getOrderByFieldOptions().contains(query.getOrderByField())) {
					throw new InvalidQueryException("Invalid orderByField: " + query.getOrderByField() + 
							", must be one of: " + QueryOptions.getOrderByFieldOptions().toString());
				}
			}
			
			//OPTIONAL: resultPackaging
			//If a value is provided
			if(query.getResultPackaging() != null) {
				
				//Check if the resultPackaging is valid
				if(!QueryOptions.getResultPackagingOptions().contains(query.getResultPackaging())) {
					throw new InvalidQueryException("Invalid resultPackaging: " + query.getResultPackaging() + 
							", must be one of: " + QueryOptions.getResultPackagingOptions().toString());
				}
			}
			
			//OPTIONAL: skip
			//Skip is parsed to Integer and validated when the query is mapped to the Query object
			
			//OPTIONAL: limit
			//Limit is parsed to Integer and validated when the query is mapped to the Query object
			
			//OPTIONAL: resultEncoding
			if(query.getResultEncoding() != null) {
				
				//Check if the result encoding is valid
				if(!QueryOptions.getResultEncodingOptions().contains(query.getResultEncoding())) {
					throw new InvalidQueryException("Invalid resultEncoding: " + query.getResultEncoding() + 
							", not one of the supported resultEncodings: " + QueryOptions.getResultEncodingOptions().toString());
				}
			}
		}
		catch (Exception e){
			throw new InvalidQueryException(e);
		}
	}

	@Override
	public List<JSONObject> forwardQuery(Query query) throws InvalidQueryException {		
		//Forward the query to the warehouse service to execute and retrieve data
		List<JSONObject> results = warehouseService.executeQuery(query);
		return results;
	}
}
