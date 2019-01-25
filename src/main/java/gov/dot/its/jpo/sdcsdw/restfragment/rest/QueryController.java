package gov.usdot.restfragment.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.usdot.restfragment.model.DepositRequest;
import gov.usdot.restfragment.model.DepositResponse;
import gov.usdot.restfragment.model.Query;
import gov.usdot.restfragment.model.QueryResults;

@RestController
@RequestMapping("/whtools/rest/v2")
public class QueryController {

	@RequestMapping(value="/deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DepositResponse deposit(@RequestBody DepositRequest request) {
		return null;
	}
	
	@RequestMapping(value="/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public QueryResults query(@RequestBody Query query) {
		return null;
	}
}
