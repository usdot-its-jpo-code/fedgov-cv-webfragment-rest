package gov.dot.its.jpo.sdcsdw.restfragment.model;

import java.util.List;

import org.json.JSONObject;

public class QueryResult {
	
	private List<JSONObject> results;

	/**
	 * @return the results
	 */
	public List<JSONObject> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<JSONObject> results) {
		this.results = results;
	}	
}
