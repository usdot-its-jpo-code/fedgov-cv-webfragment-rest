package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;

/**
 * Represents the Mongo connection based on the config from which it is instantiated.
 */
public class MongoClientConnection {

    private MongoConfig config;
    private Mongo mongoClient;
    private DB database;
    private boolean connected = false;
    private static final Logger logger = LoggerFactory.getLogger(MongoClientConnection.class);

    /**
     * Constructor
     * @param config The MongoConfig from which to establish the connection
     */
    public MongoClientConnection(MongoConfig config) {
        this.config = config;
    }

    /**
     * Establish the Mongo connection based on initial config
     * @throws MongoException
     */
    public void connect() throws MongoException {
        try {
            this.mongoClient = new Mongo(config.host, config.port);
            database = mongoClient.getDB(config.database);
            connected = true;
            logger.info("Connected to the " + config.systemName + " MongoDB "
                    + config.host + ":" + config.port);
        } catch (MongoException e) {
            logger.error("Failed to connect to MongoDB", e);
            throw e;
        }
    }    

    /**
     * Close the mongo connection
     */
    public void close() {
        mongoClient.close();
        logger.info("Closed connection to " + config.systemName + " MongoDB " + config.host + ":" + config.port);
    }

    /**
     * @return the config
     */
    public MongoConfig getConfig() {
        return config;
    }

    /**
     * @return the mongoClient
     */
    public Mongo getMongoClient() {
        return mongoClient;
    }

    /**
     * @return the database
     */
    public DB getDatabase() {
        return database;
    }
    

}
