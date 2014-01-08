package org.eluder.jetty.cli;

import org.eclipse.jetty.server.Server;
import org.eluder.jetty.server.EmbeddedJetty;
import org.eluder.jetty.server.ServerConfig;

/**
 * Command line interface for embedded Jetty.
 */
public final class EmbeddedJettyCli extends EmbeddedJetty {
    
    public EmbeddedJettyCli(final ServerConfig serverConfig) {
        super(serverConfig);
    }
    
    @Override
    protected void start(final Server server) throws Exception {
        server.start();
        server.join();
    }
    
    public static void main(final String[] args) throws Exception {
        new CliServerContext(args).start();
    }
}
