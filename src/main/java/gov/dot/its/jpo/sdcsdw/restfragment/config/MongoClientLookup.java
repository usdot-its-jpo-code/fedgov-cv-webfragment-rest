package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;

/**
 * Establish Mongo connections based on Mongo configuration file and create
 * association with system name
 *
 */
@Component
public class MongoClientLookup {

	private MongoConfigLoader mongoConfigLoader;
	private Map<String, MongoClientConnection> connections;
	
	/**
	 * Constructor, initializing the connections
	 * @param mongoConfigLoader
	 */
	@Autowired
	public MongoClientLookup(MongoConfigLoader mongoConfigLoader) {
		this.mongoConfigLoader = mongoConfigLoader;
		this.connections = initializeConnections(this.mongoConfigLoader.getMongoConfigList());
	}
	
	/**
	 * Initialize Mongo connections based on the MongoConfigLoader's list of configurations
	 */
	private static Map<String, MongoClientConnection> initializeConnections(List<MongoConfig> configList) {

		Map<String, MongoClientConnection> connections = new HashMap<String, MongoClientConnection>();
		
		//For each config, create a new connection, connect, and put in the map to associate
		//config system name with the connection
		for(MongoConfig config : configList) {
			MongoClientConnection connection = new MongoClientConnection(config);
			connection.connect();
			connections.put(config.systemName, connection);
		}
		
		return connections;
	}
	
	/**
	 * Get the Mongo connection associated with a system name
	 * @param sysname
	 * @return MongoClientConnection
	 */
	public MongoClientConnection lookupMongoClient(String sysname) {
		
		return connections.get(sysname);
		
	}
}
