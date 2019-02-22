package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface WarehouseService {

    // Execute the query on data store
    public List<JsonNode> executeQuery(Query query) throws InvalidQueryException, IOException;
}
