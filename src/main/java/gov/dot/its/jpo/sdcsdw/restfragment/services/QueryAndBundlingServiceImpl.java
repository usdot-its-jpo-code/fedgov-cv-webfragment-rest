package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.RawPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResult;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

/**
 * Implementation of the QueryAndBunldingService interface.
 */
@Service
@Primary
public class QueryAndBundlingServiceImpl implements QueryAndBundlingService {

    private QueryService queryService;
    private BundlingService bundleService;
    private EncoderService encoderService;
    private final static Logger logger = LoggerFactory.getLogger(QueryAndBundlingServiceImpl.class);

    /**
     * Constructor
     * @param queryService
     * @param bundleService
     * @param encoderService
     */
    @Autowired
    public QueryAndBundlingServiceImpl(QueryService queryService, BundlingService bundleService, EncoderService encoderService) {
        this.queryService = queryService;
        this.bundleService = bundleService;
        this.encoderService = encoderService;
    }

    // First query through query service
    @Override
    public QueryResult queryAndBundle(Query query) throws CodecFailedException,
            FormattingFailedException, UnformattingFailedException, IOException,
            DecoderException, JAXBException, InvalidQueryException {

        // Set default query values, if necessary, and perform query validation
        queryService.setDefaults(query);
        queryService.validateQuery(query);

        // Perform the query, getting a list of JSON objects
        List<JsonNode> queryResults = queryService.forwardQuery(query);

        // Pass query results to bundling service
        List<JsonNode> postPackageResults = bundleService.bundleOrDistribute(queryResults, query.getResultPackaging(), query.getDialogId());

        // Pass the packaged results to the encoding service
        List<JsonNode> postEncodedResults = encoderService.encodeBundles(postPackageResults, query.getResultPackaging(), query.getResultEncoding());

        // Wrap the post encoded results in a QueryResult object and return
        QueryResult qr = new QueryResult();
        qr.setResults(postEncodedResults);
        return qr;
    }
}
