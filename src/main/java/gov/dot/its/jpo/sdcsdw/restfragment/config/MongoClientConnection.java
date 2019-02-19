package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.CloseableInsertSitDataDao;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoOptionsBuilder;

public class MongoClientConnection {

    private MongoConfig config;
    private Mongo mongoClient;
    private DB database;
    private boolean connected = false;
    private CloseableInsertSitDataDao dao;
    private static final Logger logger = LoggerFactory.getLogger(MongoClientConnection.class);

    public MongoClientConnection(MongoConfig config) {
        this.config = config;
    }

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
    
    public void connectDeposit() throws UnknownHostException, MongoException {
        try {
            MongoOptions options = new MongoOptionsBuilder().setAutoConnectRetry(config.autoConnectRetry).setConnectTimeoutMs(config.connectionTimeoutMs).build();
            this.dao = CloseableInsertSitDataDao.newInstance(config.host, config.port, options, config.database);
            logger.info("Connected to the " + config.systemName + " MongoDB " + config.host + ":" + config.port);
        } catch (UnknownHostException e) {
            logger.error("Failed to connect to MongoDB", e);
            throw e;
        } catch (MongoException e) {
            logger.error("Failed to connect to MongoDB", e);
            throw e;
        }
    }

    public void close() {
        mongoClient.close();
    }
    
    public void depositConnectionClose() {
        dao.close();
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
    
    /**
     * @return the DAO.
     */
    public CloseableInsertSitDataDao getDao() {
        return dao;
    }
}
