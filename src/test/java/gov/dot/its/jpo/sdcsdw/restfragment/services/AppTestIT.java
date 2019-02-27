package gov.dot.its.jpo.sdcsdw.restfragment.services;


import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.store.HttpProxyFactory;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.runtime.Network;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.restfragment.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
@TestPropertySource(properties = { "mongoConfigFile:src/test/resources/it-mongo.config" })
public class AppTestIT
{
    private static MongodExecutable mongodExecutable;
    private static MongoClient mongoClient;
    private static MongoCollection<DBObject> travelerInformation;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext context;
    
    @BeforeClass
    public static void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;
        String dbName = "cvdb";
        String collectionName = "travelerInformation";
 
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.V3_4)
            .net(new Net(ip, port, Network.localhostIsIPv6()))
            .build();
 

        MongodStarter starter = MongodStarter.getDefaultInstance();
            
        
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoClient = new MongoClient(ip, port);
        
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        travelerInformation = mongoDatabase.getCollection(collectionName, JsonNode.class);
    }
    
    public void cleanDatabase() throws Exception {
        travelerInformation.deleteMany(new BasicDBObject());
    }
    
    public void setupMockMvc() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Before
    public void setupTest() throws Exception {
        cleanDatabase();
        setupMockMvc();
    }
    
    @AfterClass
    public static void clean() throws Exception {
        mongoClient.close();
        mongodExecutable.stop();
    }
    
    @Test
    public void testQueryEmptyDatabase() throws Exception {
        genericQueryTest("/it/initial-database-contents/empty.json", "/it/query/full-no-bundling.json", "/it/query-results/empty.json");
    }
    
    @Test
    public void testQuerySingleHex() throws Exception {
        genericQueryTest("/it/initial-database-contents/single-hex.json", "/it/query/full-no-bundling.json", "/it/query-results/full-no-bundling-single-hex.json");
    }
    
    private void genericQueryTest(String databaseContentsResourcePath, String queryResourcePath, String expectedResultResourcePath) throws Exception {
        genericQueryTest(loadJsonListFromResource(databaseContentsResourcePath), loadJsonFromResource(queryResourcePath), loadJsonFromResource(expectedResultResourcePath));
    }
    
    private void genericDepositTest(String initialDatabaseContentsResourcePath, String depositResourcePath, String expectedResultResourcePath, String expectedInDatabaseResourcePath) throws Exception {
        genericDepositTest(loadJsonListFromResource(initialDatabaseContentsResourcePath), loadJsonFromResource(depositResourcePath), loadJsonFromResource(expectedResultResourcePath), loadJsonListFromResource(expectedInDatabaseResourcePath));
    }
    
    
    private void genericQueryTest(List<JsonNode> databaseContents, JsonNode query, JsonNode expectedResult) throws Exception {
        for (JsonNode asd : databaseContents) {
            insertAsd(asd);
        }
        
        queryAndAssertResult(query, expectedResult);
    }
    
    private void genericDepositTest(List<JsonNode> initialDatabaseContents, JsonNode deposit, JsonNode expectedResult, List<JsonNode> expectedInDatabase) throws Exception {
        for (JsonNode asd : initialDatabaseContents) {
            insertAsd(asd);
        }
        
        depositAndAssertResult(deposit, expectedResult);
        
        for (JsonNode expectedAsd : expectedInDatabase) {
            assertMongoHasAsd(expectedAsd);
        }
    }
    
    
    
    private void insertAsd(JsonNode asd) throws Exception {
        travelerInformation.insertOne((DBObject)JSON.parse(mapper.writeValueAsString(asd)));
    }
    
    private void assertMongoHasAsd(JsonNode expected) throws Exception {
        BasicDBObject filter = new BasicDBObject();
        
        Iterable<Map.Entry<String, JsonNode>> fields = () -> expected.fields();
        
        for (Map.Entry<String, JsonNode> entry : fields) {
            if (!entry.getValue().isContainerNode()) {
                filter.put(entry.getKey(), entry.getValue());
            }
        }
        
        Set<JsonNode> matchSet = 
                StreamSupport
                    .stream(travelerInformation.find(filter).spliterator(), false)
                    .map((x) -> { try { return mapper.readTree(JSON.serialize(x)); } catch (Exception e) { throw new RuntimeException(e); } })
                    .collect(Collectors.toSet());
        
        assertTrue(matchSet.contains(expected));
    }
    
    private void queryAndAssertResult(JsonNode query, JsonNode expectedResult) throws Exception {
        MockHttpServletRequestBuilder request =
                post("/v2/query")
                    .content(mapper.writeValueAsString(query))
                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }
    
    private void depositAndAssertResult(JsonNode deposit, JsonNode expectedResult) throws Exception {
        MockHttpServletRequestBuilder request =
                post("/v2/deposit")
                    .content(mapper.writeValueAsString(deposit))
                    .contentType(MediaType.APPLICATION_JSON_VALUE);
        
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }
    
    private JsonNode loadJsonFromResource(String resourcePath) throws Exception {
        return mapper.readTree(AppTestIT.class.getResourceAsStream(resourcePath));
    }
    
    private List<JsonNode> loadJsonListFromResource(String resourcePath) throws Exception {
        JsonNode jsonListNode = loadJsonFromResource(resourcePath);
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(jsonListNode.elements(), Spliterator.ORDERED), false)
                .collect(Collectors.toList());
    }
}
