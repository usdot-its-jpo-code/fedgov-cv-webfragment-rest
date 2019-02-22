package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Access the property value associated with the Mongo configurations
 *
 */
@Component
public class MongoConfigFileProperty {

    //Update: the same mongoConfigFile property is used for both queries and deposits
    @Value("${mongoConfigFile}")
    private String mongoConfigFilePropertyValue;

    /**
     * Returns the property value for the Mongo configuration file used by queries
     * 
     * @return String of the property value
     */
    public String getMongoConfigFilePropertyValue() {
        return mongoConfigFilePropertyValue;
    }
}
