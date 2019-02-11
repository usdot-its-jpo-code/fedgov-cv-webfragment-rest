package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Access the property value associated with the Mongo configurations
 *
 */
@Component
public class MongoConfigFileProperty {

    @Value("${mongoConfigFile}")
    private String mongoConfigFilePropertyValue;

    /**
     * Returns the property value for the Mongo configuration file
     * 
     * @return String of the property value
     */
    public String getMongoConfigFilePropertyValue() {
        return mongoConfigFilePropertyValue;
    }
}
