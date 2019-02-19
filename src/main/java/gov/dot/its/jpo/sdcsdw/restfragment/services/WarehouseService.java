package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface WarehouseService {

    // Execute the query on data store
    public List<JsonNode> executeQuery(Query query) throws InvalidQueryException, IOException;
    
    //Execute the deposit request on data store
    public int executeDeposit(DepositRequest request);
}
