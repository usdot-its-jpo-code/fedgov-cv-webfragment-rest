package gov.dot.its.jpo.sdcsdw.restfragment.services;

import org.apache.commons.codec.DecoderException;
import org.w3c.dom.Document;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecException;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;

public interface DepositService {

    /**
     * Validate the deposit request
     * @param request the DepositRequest
     * @throws DepositException
     */
    public void validateDeposit(DepositRequest request) throws DepositException;
    
    /**
     * Prepare the request for deposit
     * @param request the DepositRequest
     * @return the prepared Document for deposit
     * @throws DepositException
     * @throws DecoderException
     * @throws CodecException
     */
    public Document prepareDeposit(DepositRequest request) throws DepositException, DecoderException, CodecException;
    
    /**
     * Execute the deposit
     * @param request the original DepositRequest
     * @param xer the Document for deposit
     * @return DepositResponse indicating number of deposits
     * @throws DepositException
     */
    public DepositResponse executeDeposit(DepositRequest request, Document xer) throws DepositException;
}
