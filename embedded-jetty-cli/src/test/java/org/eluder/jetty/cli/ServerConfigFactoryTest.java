package org.eluder.jetty.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.eluder.jetty.server.ServerConfig;
import org.junit.Test;

public class ServerConfigFactoryTest {

    @Test(expected = MissingOptionException.class)
    public void testCreateWithoutArgs() throws Exception {
        createServerConfig();
    }
    
    @Test
    public void testCreateWithDefaults() throws Exception {
        ServerConfig serverConfig = createServerConfig("-webApp", "./app.war");
        assertFalse(serverConfig.isClasspath());
        assertFalse(serverConfig.isPlus());
        assertEquals("./app.war", serverConfig.getWebApp());
    }
    
    @Test
    public void testCreateWithArguments() throws Exception {
        ServerConfig serverConfig = createServerConfig("-plus", "-minThreads", "5", "-webApp", "./app.war");
        assertFalse(serverConfig.isClasspath());
        assertTrue(serverConfig.isPlus());
        assertEquals(5, serverConfig.getMinThreads());
        assertEquals("./app.war", serverConfig.getWebApp());
    }
    
    private ServerConfig createServerConfig(final String... args) throws Exception {
        return new ServerConfigFactory(ServerConfig.class, createCommandLine(args)).create();
    }
    
    private CommandLine createCommandLine(final String... args) throws Exception {
        Options options = new ServerConfigOptionsFactory(ServerConfig.class).create();
        return new BasicParser().parse(options, args);
    }
    
}
