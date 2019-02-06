package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;

public interface QueryAndBundlingService {
	
	//This could change, figure out what the type of list is
	public List<T> queryAndBundle(Query query);
}
