package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;
import com.mongodb.MongoOptions;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.CloseableInsertSitDataDao;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoOptionsBuilder;

public class MongoClientDepositConnection {
    
    private MongoConfig config;
    private CloseableInsertSitDataDao dao;
    private static final Logger logger = LoggerFactory.getLogger(MongoClientDepositConnection.class);
    
    public MongoClientDepositConnection(MongoConfig config) {
        this.config = config;
    }

    public void connect() throws UnknownHostException, MongoException {
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
     * @return the DAO.
     */
    public CloseableInsertSitDataDao getDao() {
        return dao;
    }
}
