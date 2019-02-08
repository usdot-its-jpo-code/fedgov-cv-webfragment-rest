package gov.dot.its.jpo.sdcsdw.restfragment.rest;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResult;
import gov.dot.its.jpo.sdcsdw.restfragment.services.QueryAndBundlingService;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

@RestController
@RequestMapping("/whtools/rest/v2")
public class QueryController {
	
	private QueryAndBundlingService queryAndBundle;
	private static final Logger logger = Logger.getLogger(QueryController.class.getName());
	
	@Autowired
	public QueryController (QueryAndBundlingService queryAndBundle) {
		this.queryAndBundle = queryAndBundle;
	}
	
	@RequestMapping(value="/deposit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public DepositResponse deposit(@RequestBody DepositRequest request) {
		return null;
	}
	
	@RequestMapping(value="/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public QueryResult query(@RequestBody Query query) {
		
		// Use combined service to get the results
		try {
			QueryResult result = queryAndBundle.queryAndBundle(query);
		} catch (InvalidQueryException e) {
			
		}

		return null;
	}
}
