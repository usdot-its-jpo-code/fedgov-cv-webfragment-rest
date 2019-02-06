package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;

public interface WarehouseService {

	//Execute the query on data store
	public List<AdvisorySituationData> executeQuery(Query query);
}
