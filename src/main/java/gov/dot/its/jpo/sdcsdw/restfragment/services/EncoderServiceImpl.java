package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Type;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.Asn1Types;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.PerXerCodec;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.CodecFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.FormattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.exception.UnformattingFailedException;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.Base64PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.HexPerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataFormatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.per.PerDataUnformatter;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.RawXerData;
import gov.dot.its.jpo.sdcsdw.asn1.perxercodec.xer.XerDataFormatter;
import gov.dot.its.jpo.sdcsdw.xerjaxbcodec.XerJaxbCodec;

@Service
public class EncoderServiceImpl implements EncoderService {
    
    private final static Logger logger = LoggerFactory.getLogger(EncoderServiceImpl.class);

    private static JAXBContext jaxbContext = null;
    private static Marshaller marshaller = null;
    
    static {
        try {
            jaxbContext = JAXBContext.newInstance("gov.dot.its.jpo.sdcsdw.Models");
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        } catch (JAXBException e1) {
            throw new RuntimeException(e1);
        }
    }

    
    public List<JsonNode> encodeBundles(List<JsonNode> postPackageResults,
                                   String resultPackaging, String resultEncoding)  {
     
        
        logger.info("Encoding with " + resultEncoding + " and packaging " + resultPackaging);
        
        List<JsonNode> encodedResults = new ArrayList<>();
        
        ObjectMapper mapper = new ObjectMapper();
        
        PerDataFormatter<String, ?> formatter;
        
        switch (resultEncoding) {
        case "hex":
            formatter = HexPerData.formatter;
            break;
        case "base64":
            formatter = Base64PerData.formatter;
            break;
        default:  
            formatter = null;
            break;
        }
        
        Class<?> typeClass;
        Asn1Type asn1Type;
        
        switch(resultPackaging) {
        case "bundle":
            typeClass = AdvisorySituationBundle.class;
            asn1Type = Asn1Types.AdvisorySituationBundleType;
            break;
        case "distribution":
            typeClass = AdvisorySituationDataDistribution.class;
            asn1Type = Asn1Types.AdvisorySituationDataDistributionType;
            break;
        default:
            typeClass = AdvisorySituationData.class;
            asn1Type = Asn1Types.AdvisorySituationDataType;
            break; 
        }
        
        for (JsonNode node : postPackageResults) {
            switch (resultEncoding) {
            case "hex":
            case "base64":
                StringWriter writer = new StringWriter();
                try {
                    marshaller.marshal(mapper.treeToValue(node, typeClass), writer);
                    String xer = XerJaxbCodec.createSelfClosingTags(writer.toString());
                    logger.info("Got XER " + xer);
                    JsonNode stringNode = new TextNode(PerXerCodec.xerToPer(asn1Type, xer, RawXerData.unformatter, formatter));
                    encodedResults.add(stringNode);
                } catch (JsonProcessingException | JAXBException | UnformattingFailedException | CodecFailedException | FormattingFailedException e) {
                    throw new IllegalArgumentException(e);
                }
                break;
            default:  
                encodedResults.add(node);
                break;
            }
        }
        
        
        return encodedResults;
    }
}
