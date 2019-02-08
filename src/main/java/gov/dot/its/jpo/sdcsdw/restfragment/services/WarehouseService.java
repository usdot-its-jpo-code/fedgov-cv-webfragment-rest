package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface WarehouseService {

	//Execute the query on data store
	public List<String> executeQuery(Query query) throws InvalidQueryException;
	
	//Check if system name is valid
	public boolean validateSystemName(String sysname);
}
