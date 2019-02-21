package gov.dot.its.jpo.sdcsdw.restfragment.util;

import com.fasterxml.jackson.databind.JsonNode;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.Base64PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.HexPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataFormatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataUnformatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.SpacelessHexPerData;

public class EncodeTypeUtil
{
    public static PerDataFormatter<String, ? extends PerData<String>> getFormatter(JsonNode json, boolean spaceless) {
        String encodeType = json.get("encodeType").asText();
        return getFormatter(encodeType, spaceless);
    }
    
    public static PerDataFormatter<String, ? extends PerData<String>> getFormatter(JsonNode json) {
        return getFormatter(json, false);
    }
    
    public static PerDataFormatter<String, ? extends PerData<String>> getFormatter(String encodeType, boolean spaceless) {
        switch(encodeType.toLowerCase()) {
        case "hex":
        case "uper":
            if (spaceless) {
                return SpacelessHexPerData.formatter;
            } else {
                return HexPerData.formatter;
            }
        case "base64":
            return Base64PerData.formatter;
        default:
            throw new IllegalArgumentException();    
        }
    }
    
    public static PerDataFormatter<String, ? extends PerData<String>> getFormatter(String encodeType) {
        return getFormatter(encodeType, false);
    }
    
    public static PerDataUnformatter<String, ? extends PerData<String>> getUnformatter(String encodeType) {
        switch(encodeType.toLowerCase()) {
        case "hex":
        case "uper":
            return HexPerData.unformatter;
        case "base64":
            return Base64PerData.unformatter;
        default:
            throw new IllegalArgumentException();    
        }
    }
    
    public static PerDataUnformatter<String, ? extends PerData<String>> getUnformatter(JsonNode json) {
        String encodeType = json.get("encodeType").asText();
        return getUnformatter(encodeType);
    }
}
