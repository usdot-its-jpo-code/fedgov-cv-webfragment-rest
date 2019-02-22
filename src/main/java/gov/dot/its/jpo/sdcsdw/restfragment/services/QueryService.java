package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface QueryService {

    // Set defaults
    public void setDefaults(Query query);

    // Validate query
    public void validateQuery(Query query) throws InvalidQueryException;

    // Execute the query
    public List<JsonNode> forwardQuery(Query query) throws InvalidQueryException, IOException;
}
