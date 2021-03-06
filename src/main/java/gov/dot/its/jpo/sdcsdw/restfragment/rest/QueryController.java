package gov.dot.its.jpo.sdcsdw.restfragment.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositRequest;
import gov.dot.its.jpo.sdcsdw.restfragment.model.DepositResponse;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResult;
import gov.dot.its.jpo.sdcsdw.restfragment.services.DepositService;
import gov.dot.its.jpo.sdcsdw.restfragment.services.QueryAndBundlingService;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit.DepositException;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

/**
 * Controller for query and deposit endpoints
 */
@RestController
@RequestMapping("/v2")
public class QueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);
    private QueryAndBundlingService queryAndBundle;
    private DepositService deposit;

    /**
     * Constructor for the controller
     * @param queryAndBundle The service used to query and bundle
     * @param deposit The depositing service
     */
    @Autowired
    public QueryController(QueryAndBundlingService queryAndBundle, DepositService deposit) {
        this.queryAndBundle = queryAndBundle;
        this.deposit = deposit;
    }

    /**
     * Endpoint for making deposits
     * @param request the DepositRequest
     * @return DepositResponse indicating the number of deposits
     * @throws DepositException
     * @throws DecoderException
     * @throws CodecException
     */
    @RequestMapping(value = "/deposit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DepositResponse deposit(@RequestBody DepositRequest request) throws DepositException, DecoderException, CodecException {
        
        deposit.validateDeposit(request);
        Document xer = deposit.prepareDeposit(request);
        DepositResponse response = deposit.executeDeposit(request, xer);
        return response;
    }

    /**
     * Endpoint for making queries
     * @param query the Query
     * @return the QueryResult
     * @throws CodecFailedException
     * @throws FormattingFailedException
     * @throws UnformattingFailedException
     * @throws IOException
     * @throws DecoderException
     * @throws JAXBException
     * @throws InvalidQueryException
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public QueryResult query(@RequestBody Query query)
            throws CodecFailedException, FormattingFailedException,
            UnformattingFailedException, IOException, DecoderException,
            JAXBException, InvalidQueryException {

        // Use combined service to get the results
        QueryResult result = queryAndBundle.queryAndBundle(query);

        return result;
    }

    /**
     * Endpoint for health checks
     * @return "alive" as an indicator response
     */
    @GetMapping("/health")
    public String health() {
        return "alive";
    }
}
