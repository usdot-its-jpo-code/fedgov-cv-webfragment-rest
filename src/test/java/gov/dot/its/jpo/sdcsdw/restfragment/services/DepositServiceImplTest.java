package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;

public class DepositServiceImplTest {

    private DepositRequest request;
    private DepositServiceImpl impl;
    
    @Before
    public void setup() {
        this.impl = new DepositServiceImpl(null);
        this.request = new DepositRequest();
        this.request.setSystemDepositName("SDW 2.3");
        this.request.setEncodeType("hex");
        this.request.setEncodeMsg("placeholder");
    }
    
    //No systemDepositName
    @Test(expected = DepositException.class)
    public void testValidateNoSystemDepositName() throws Exception  {
        request.setSystemDepositName(null);
        impl.validateDeposit(request);
    }
    
    //No encodeType
    @Test(expected = DepositException.class)
    public void testValidateNoEncodeType() throws Exception {
        request.setEncodeType(null);
        impl.validateDeposit(request);
    }
    
    //No encodeMsg
    @Test(expected = DepositException.class)
    public void testValidateNoEncodeMsg() throws Exception {
        request.setEncodeMsg(null);
        impl.validateDeposit(request);
    }
    
    //Invalid encodeType
    @Test(expected = DepositException.class)
    public void testValidateWrongEncodeType() throws Exception {
        request.setEncodeType("default");
        impl.validateDeposit(request);
    }
}
