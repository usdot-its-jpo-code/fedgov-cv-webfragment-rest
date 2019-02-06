package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

@Service
@Primary
public class QueryAndBundlingServiceImpl implements QueryAndBundlingService{

	private QueryService queryService;
	private BundlingService bundleService;
	
	@Autowired
	public QueryAndBundlingServiceImpl(QueryService queryService, BundlingService bundleService) {
		this.queryService = queryService;
		this.bundleService = bundleService;
	}

	
	//Define method queryAndBundle(Query query)
	//First query through query service
	@Override
	public List<String> queryAndBundle(Query query) {
		// TODO Auto-generated method stub
		queryService.validateQuery(query);
		List<String> results = queryService.forwardQuery(query);
		return null;
	}
	
}
