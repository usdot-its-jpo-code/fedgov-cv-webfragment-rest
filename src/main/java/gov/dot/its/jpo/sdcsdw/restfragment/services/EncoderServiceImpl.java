package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;

public class EncoderServiceImpl implements EncoderService {

	@Override
	public List<JsonNode> encodeBundles(List<JsonNode> jsonNodes, String resultEncoding) {
		return jsonNodes;
	}

}
