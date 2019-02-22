package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface QueryService {

    /**
     * Set query defaults
     * @param query the Query
     */
    public void setDefaults(Query query);

    /**
     * Validate the query request
     * @param query the Query
     * @throws InvalidQueryException
     */
    public void validateQuery(Query query) throws InvalidQueryException;

    /**
     * Execute the query request
     * @param query the Query
     * @return the list of results as JSONNode objects
     * @throws InvalidQueryException
     * @throws IOException
     */
    public List<JsonNode> forwardQuery(Query query) throws InvalidQueryException, IOException;
}
