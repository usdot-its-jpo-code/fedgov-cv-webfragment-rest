package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.utils.ConfigUtils;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.utils.ConfigurationException;

/**
 * Transition the contents of the Mongo configuration files into a list of
 * MongoConfig.
 *
 */
@Component
public class MongoConfigLoader {

    private MongoConfigFileProperty mongoConfigFileProperty;
    private List<MongoConfig> configList;
    private List<MongoConfig> depositConfigList;
    private static final Logger logger = LoggerFactory.getLogger(MongoConfigLoader.class);

    /**
     * Constructor
     * 
     * @param mongoConfigFileProperty
     */
    @Autowired
    public MongoConfigLoader(MongoConfigFileProperty mongoConfigFileProperty) throws ConfigurationException {
        
        this.mongoConfigFileProperty = mongoConfigFileProperty;
        try {
            configList = ConfigUtils.loadConfigBeanList(this.mongoConfigFileProperty.getMongoConfigFilePropertyValue(), MongoConfig.class);
            depositConfigList = ConfigUtils.loadConfigBeanList(this.mongoConfigFileProperty.getMongoDepositConfigFilePropertyValue(), MongoConfig.class);
        } catch (ConfigurationException e) {
            logger.error("Error loading Mongo config list", e);
            throw e;
        }
    }

    /**
     * Get the list of Mongo query configurations based on the query config file
     * 
     * @return list of MongoConfig
     */
    public List<MongoConfig> getMongoConfigList() {

        return configList;
    }
    
    /**
     * Get the list of Mongo deposit configurations based on the deposit config file
     * 
     * @return list of MongoConfig
     */
    public List<MongoConfig> getMongoDepositConfigList() {
        
        return depositConfigList;
    }
}
