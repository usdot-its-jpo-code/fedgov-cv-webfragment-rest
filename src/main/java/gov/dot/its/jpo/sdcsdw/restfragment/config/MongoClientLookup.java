package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;

/**
 * Establish Mongo connections based on Mongo configuration files and create
 * association with system name
 *
 */
@Component
public class MongoClientLookup {

    private MongoConfigLoader mongoConfigLoader;
    //Query connections
    private Map<String, MongoClientConnection> connections;
    //Deposit connections
    private Map<String, MongoClientConnection> depositConnections;

    /**
     * Constructor, initializing the connections
     * 
     * @param mongoConfigLoader
     * @throws MongoException 
     * @throws UnknownHostException 
     */
    @Autowired
    public MongoClientLookup(MongoConfigLoader mongoConfigLoader) throws UnknownHostException, MongoException {
        this.mongoConfigLoader = mongoConfigLoader;
        this.connections = initializeConnections(this.mongoConfigLoader.getMongoConfigList());
        this.depositConnections = initializeDepositConnections(this.mongoConfigLoader.getMongoDepositConfigList());
    }

    /**
     * Initialize Mongo connections based on the MongoConfigLoader's list of
     * configurations
     */
    private static Map<String, MongoClientConnection> initializeConnections(List<MongoConfig> configList) {

        Map<String, MongoClientConnection> connections = new HashMap<String, MongoClientConnection>();

        // For each config, create a new connection, connect, and put in the map
        // to associate config system name with the connection
        for (MongoConfig config : configList) {
            MongoClientConnection connection = new MongoClientConnection(config);
            connection.connect();
            connections.put(config.systemName, connection);
        }

        return connections;
    }
    
    private static Map<String, MongoClientConnection> initializeDepositConnections(List<MongoConfig> configList) throws UnknownHostException, MongoException {
        
        Map<String, MongoClientConnection> connections = new HashMap<String, MongoClientConnection>();
        
        //For each config, create a new connection, connect, and put in the map
        for (MongoConfig config : configList) {
            MongoClientConnection connection = new MongoClientConnection(config);
            connection.connectDeposit();
            connections.put(config.systemName, connection);
        }
        
        return connections;
    }

    /**
     * Get the Mongo query connection associated with a system name
     * 
     * @param sysname
     * @return MongoClientConnection
     */
    public MongoClientConnection lookupMongoClient(String sysname) {

        return connections.get(sysname);

    }
    
    /**
     * Get the Mongo deposit connection associated with a system name
     * @param sysname
     * @return MongoClientConnection
     */
    public MongoClientConnection lookupMongoDepositClient(String sysname) {
        
        return depositConnections.get(sysname);
    }
}
