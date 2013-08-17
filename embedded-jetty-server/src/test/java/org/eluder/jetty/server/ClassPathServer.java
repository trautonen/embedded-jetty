package org.eluder.jetty.server;

public class ClassPathServer {

    public static void main(final String[] args) throws Exception {
        ServerConfig serverConfig = new ServerConfig().setClassPath(true);
        new EmbeddedJetty(serverConfig).run();
    }
    
}
