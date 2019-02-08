package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.utils.ConfigUtils;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.utils.ConfigurationException;

/**
 * Transition the contents of the Mongo configuration file into a list of MongoClients
 *
 */
@Component
public class MongoConfigLoader {

	private MongoConfigFileProperty mongoConfigFileProperty;
	private static final Logger logger = Logger.getLogger(MongoConfigLoader.class.getName());
	
	/**
	 * Constructor
	 * @param mongoConfigFileProperty
	 */
	@Autowired
	public MongoConfigLoader(MongoConfigFileProperty mongoConfigFileProperty) {
		this.mongoConfigFileProperty = mongoConfigFileProperty;
	}
	
	/**
	 * Get the list of Mongo configurations based on the config file
	 * @return list of MongoConfig
	 */
	public List<MongoConfig> getMongoConfigList() {
		
		List<MongoConfig> configList = null;
		
		try {
			configList = ConfigUtils.loadConfigBeanList(mongoConfigFileProperty.getMongoConfigFilePropertyValue(), MongoConfig.class);
		} catch (ConfigurationException e) {
			logger.error("Error loading Mongo config list", e);
		}
		
		return configList;
	}
}
