package gov.dot.its.jpo.sdcsdw.restfragment.services;


import java.net.URI;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
import gov.dot.its.jpo.sdcsdw.restfragment.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
@TestPropertySource(properties = { "mongoConfigFile:src/test/resources/it-mongo.config" })
public class AppTestIT
{
    private static MongodExecutable mongodExecutable;
    
    @BeforeClass
    public static void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;
 
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.V3_4)
            .net(new Net(ip, port, Network.localhostIsIPv6()))
            .build();
 

        MongodStarter starter = MongodStarter.getDefaultInstance();
            
        
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }
    
    @AfterClass
    public static void clean() throws Exception {
        mongodExecutable.stop();
    }
    
    @Test
    public void test() throws Exception {
        
    }
}
