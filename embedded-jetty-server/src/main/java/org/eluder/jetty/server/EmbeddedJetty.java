package org.eluder.jetty.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Jetty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedJetty {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJetty.class);
    
    protected ServerConfig serverConfig;

    public EmbeddedJetty(final ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    
    protected ServerFactory createServerFactory() {
        return new ServerFactory(serverConfig);
    }
    
    public void run() throws Exception {
        Server server = createServerFactory().create();
        LOGGER.info(">>> Starting embedded Jetty {}\n{}", Jetty.VERSION, serverConfig);
        start(server);
    }
    
    protected void start(final Server server) throws Exception {
        LOGGER.info(">>> Press any key to stop");
        server.start();
        System.in.read();
        LOGGER.info(">>> Stopping embedded Jetty");
        server.stop();
        server.join();
    }
}
