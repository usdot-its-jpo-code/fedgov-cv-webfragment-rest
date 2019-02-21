package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;


public interface BundlingService {
	public List<JsonNode> bundleOrDistribute(List<JsonNode> jsonList, String packageType, String dialogID) throws JsonParseException, JsonMappingException, IOException;
}
