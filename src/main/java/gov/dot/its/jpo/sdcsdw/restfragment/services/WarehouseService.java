package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import org.json.JSONObject;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface WarehouseService {

    // Execute the query on data store
    public List<JSONObject> executeQuery(Query query) throws InvalidQueryException;
}
