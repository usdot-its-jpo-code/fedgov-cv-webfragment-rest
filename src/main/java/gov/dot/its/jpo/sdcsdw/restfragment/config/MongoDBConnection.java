package gov.dot.its.jpo.sdcsdw.restfragment.config;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;

public class MongoDBConnection {

	private MongoConfig config;
	private Mongo mongoClient;
	private DB database;
	
	public MongoDBConnection(MongoConfig config) {
		this.config = config;
	}
	
	public void connect() {
		try {
			this.mongoClient = new Mongo(config.host, config.port);
			database = mongoClient.getDB(config.database);
		} catch (MongoException e) {
			
		}
	}

	/**
	 * @return the config
	 */
	public MongoConfig getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(MongoConfig config) {
		this.config = config;
	}

	/**
	 * @return the mongoClient
	 */
	public Mongo getMongoClient() {
		return mongoClient;
	}

	/**
	 * @param mongoClient the mongoClient to set
	 */
	public void setMongoClient(Mongo mongoClient) {
		this.mongoClient = mongoClient;
	}

	/**
	 * @return the database
	 */
	public DB getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(DB database) {
		this.database = database;
	}
	
	
}
