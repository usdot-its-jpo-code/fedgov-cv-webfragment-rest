package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Access the property value associated with the Mongo configurations
 *
 */
@Component
public class MongoConfigFileProperty {

    //For queries
    @Value("${mongoConfigFile}")
    private String mongoConfigFilePropertyValue;
    
    //For deposits
    @Value("${depositConfigFile}")
    private String mongoDepositConfigFilePropertyValue;

    /**
     * Returns the property value for the Mongo configuration file used by queries
     * 
     * @return String of the property value
     */
    public String getMongoConfigFilePropertyValue() {
        return mongoConfigFilePropertyValue;
    }
    
    /**
     * Returns the property value for the Mongo configuration file used by deposits
     * 
     * @return String of the property value
     */
    public String getMongoDepositConfigFilePropertyValue() {
        return mongoDepositConfigFilePropertyValue;
    }
}
