package gov.dot.its.jpo.sdcsdw.restfragment.services;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.RawPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.DocumentXerData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.restfragment.util.DepositOptions;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;

@Service
@Primary
public class DepositServiceImpl implements DepositService {

    private WarehouseService warehouseService;
    private final static Logger logger = LoggerFactory.getLogger(QueryAndBundlingServiceImpl.class);
    
    //Required field names in DepositRequest
    private static final String SYSTEM_NAME = "systemDepositName";
    private static final String ENCODE_TYPE = "encodeType";
    private static final String ENCODED_MSG = "encodedMsg";
    
    @Autowired
    public DepositServiceImpl(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }
    
    @Override
    public boolean validateDeposit(DepositRequest request) throws DepositException {
        
        //REQUIRED: request must have all required fields
        if(request.getSystemDepositName() != null && request.getEncodeType() != null && request.getEncodeMsg() != null) {
            
            //Note: Verification of a correct system name is confirmed by the warehouse service.
            
            //REQUIRED: encodeType must be valid
            boolean valid = false;
            for(String encodeType : DepositOptions.getEncodeTypeOptions()) {
                if(request.getEncodeType().equalsIgnoreCase(encodeType)) {
                    valid = true;
                    break;
                }
            }
            if(!valid)
                throw new DepositException("Invalid encodeType: " + request.getEncodeType() + " not one of the supported "
                        + "encodeType: " + DepositOptions.getEncodeTypeOptions().toString());
            
        } else {
            String errorMsg = "Deposit message missing required field(s): ";
            if(request.getSystemDepositName() == null)
                errorMsg = errorMsg + SYSTEM_NAME + " ";
            
            if(request.getEncodeType() == null)
                errorMsg = errorMsg + ENCODE_TYPE + " ";
            
            if(request.getEncodeMsg() == null)
                errorMsg = errorMsg + ENCODED_MSG;
            
            throw new DepositException(errorMsg);
        }

        //If no exception is found, return true for valid deposit request
        return true;
    }
    
    @Override
    public Document prepareDeposit(DepositRequest request) throws DepositException, DecoderException, CodecException {
        
        //Convert encodedMsg to bytes
        byte [] bytes = null;
        if(request.getEncodeType().equalsIgnoreCase(DepositOptions.ENCODE_TYPE_HEX) || request.getEncodeType().equalsIgnoreCase(DepositOptions.ENCODE_TYPE_UPER)) {
            try {
                bytes = Hex.decodeHex(request.getEncodeMsg().toCharArray());
            } catch (DecoderException e) {
                throw new DecoderException("Hex to bytes decoding failed: " + e);
            }
        } else if (request.getEncodeType().equalsIgnoreCase(DepositOptions.ENCODE_TYPE_BASE64)) {
            bytes = Base64.decodeBase64(request.getEncodeMsg());
        }
        
        //Convert bytes to Document
        Document ret = null;
        if (bytes != null) {
            try {
                ret = PerXerCodec.perToXer(Asn1Types.AdvisorySituationDataType, bytes, RawPerData.unformatter, DocumentXerData.formatter);
            } catch (CodecException e) {
                throw new CodecException("Failed to decode message: " + e);
            }
        }
        
        if(ret != null)
            return ret;
        else
            throw new DepositException("Failure to prepare deposit of encoded message: " + request.getEncodeMsg());
    }

    @Override
    public DepositResponse executeDeposit(DepositRequest request) {
        return null;
        // TODO Auto-generated method stub
        
    }

}
