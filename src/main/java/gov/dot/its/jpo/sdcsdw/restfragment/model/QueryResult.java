package gov.dot.its.jpo.sdcsdw.restfragment.model;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * POJO for the query result
 */
public class QueryResult {

    private List<JsonNode> results;

    /**
     * @return the results
     */
    public List<JsonNode> getResults() {
        return results;
    }

    /**
     * @param results
     *            the results to set
     */
    public void setResults(List<JsonNode> results) {
        this.results = results;
    }
}
