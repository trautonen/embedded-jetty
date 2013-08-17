package org.eluder.jetty.server;

public class Webapp30Server {

    public static void main(final String[] args) throws Exception {
        ServerConfig serverConfig = new ServerConfig().setWebApp("src/test/webapp30");
        new EmbeddedJetty(serverConfig).run();
    }
    
}
