package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public interface BundlingService {
	public List<JSONObject> bundleOrDistribute(List<JSONObject> jsonList, String packageType, String dialogID) throws JsonParseException, JsonMappingException, IOException, ParseException;
	public List<AdvisorySituationBundle> createBundleList(List<AdvisorySituationData> asd, String dialogID);
	public List<AdvisorySituationDataDistribution> createDistributionList(List<AdvisorySituationData> asd, String dialogID);
}
