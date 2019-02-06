package gov.dot.its.jpo.sdcsdw.restfragment.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResults;

@RestController
@RequestMapping("/v2")
public class QueryController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    public QueryController() {}
    
	@RequestMapping(value="/deposit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public DepositResponse deposit(@RequestBody DepositRequest request) {
		return null;
	}
	
	@RequestMapping(value="/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public QueryResults query(@RequestBody Query query) {
		return null;
	}
}
