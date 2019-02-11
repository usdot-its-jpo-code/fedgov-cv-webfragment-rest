package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;

public interface EncoderService {
	public String encodeBundles(String jsonString, String resultEncoding);
}
