package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;

public interface EncoderService {
	public List<JsonNode> encodeBundles(List<JsonNode> jsonNodes, String resultEncoding);
}
