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
        } catch (ConfigurationException e) {
            logger.error("Error loading Mongo config list", e);
            throw e;
        }
    }

    /**
     * Get the list of Mongo configurations based on the config file
     * 
     * @return list of MongoConfig
     */
    public List<MongoConfig> getMongoConfigList() {

        return configList;
    }
}
