package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface WarehouseService {

    /**
     * Execute the query
     * @param query the Query
     * @return the list of results as JSONNode objects
     * @throws InvalidQueryException
     * @throws IOException
     */
    public List<JsonNode> executeQuery(Query query) throws InvalidQueryException, IOException;
    
    /**
     * Execute the deposit
     * @param request the DepositRequest
     * @param xer the Document for deposit
     * @return the number of deposits made
     * @throws DepositException
     */
    public int executeDeposit(DepositRequest request, Document xer) throws DepositException;
}
