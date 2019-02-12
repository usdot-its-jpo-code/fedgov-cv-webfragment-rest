package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;


import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.Base64PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.HexPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataUnformatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.XerDataFormatter;


public class EncoderServiceImpl implements EncoderService {

	@Override
	public String perToXer(String per) throws UnformattingFailedException, CodecFailedException, FormattingFailedException {
		XerDataFormatter<String, RawXerData> xerFormatter = RawXerData.formatter;
        PerDataUnformatter<String, PerData<String>> perUnformatter = Base64PerData::new;
        
        
        return PerXerCodec.perToXer(Asn1Types.AdvisorySituationDataType, per, perUnformatter, xerFormatter);
	}

}
