package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.fasterxml.jackson.databind.JsonNode;


public interface BundlingService {
	public List<JsonNode> bundleOrDistribute(List<JsonNode> jsonList, String packageType, String dialogID) throws JsonParseException, JsonMappingException, IOException;
}
