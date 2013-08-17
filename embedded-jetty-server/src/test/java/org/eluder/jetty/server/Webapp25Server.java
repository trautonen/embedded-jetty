package org.eluder.jetty.server;

public class Webapp25Server {

    public static void main(final String[] args) throws Exception {
        ServerConfig serverConfig = new ServerConfig().setWebApp("src/test/webapp25");
        new EmbeddedJetty(serverConfig).run();
    }
    
}
