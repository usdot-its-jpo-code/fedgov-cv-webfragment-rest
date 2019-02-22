package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.DecoderException;

import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.model.QueryResult;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public interface QueryAndBundlingService {

    /**
     * Combine query and bundling operations to complete the query request
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
    public QueryResult queryAndBundle(Query query) throws CodecFailedException,
            FormattingFailedException, UnformattingFailedException, IOException,
            DecoderException, JAXBException, InvalidQueryException;
}
