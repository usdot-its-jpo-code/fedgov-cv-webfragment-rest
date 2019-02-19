package gov.dot.its.jpo.sdcsdw.restfragment.services;

import org.apache.commons.codec.DecoderException;
import org.w3c.dom.Document;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecException;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;

public interface DepositService {

    public boolean validateDeposit(DepositRequest request) throws DepositException;
    
    public Document prepareDeposit(DepositRequest request) throws DepositException, DecoderException, CodecException;
    
    public DepositResponse executeDeposit(DepositRequest request, Document xer) throws DepositException;
}
