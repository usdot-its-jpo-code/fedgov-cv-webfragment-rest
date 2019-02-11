package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;


public interface BundlingService {
	public List<JsonNode> bundleOrDistribute(List<JSONObject> jsonList, String packageType, String dialogID) throws JsonParseException, JsonMappingException, IOException, JSONException;
	public List<AdvisorySituationBundle> createBundleList(List<AdvisorySituationData> asd, String dialogID);
	public List<AdvisorySituationDataDistribution> createDistributionList(List<AdvisorySituationData> asd, String dialogID);
}
