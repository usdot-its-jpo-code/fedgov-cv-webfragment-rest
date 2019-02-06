package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface QueryAndBundlingService {
	
	//This could change, figure out what the type of list is
	public List<String> queryAndBundle(Query query);
}
