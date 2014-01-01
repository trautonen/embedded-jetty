package org.eluder.jetty.server;

public class ClasspathServer {

    public static void main(final String[] args) throws Exception {
        ServerConfig serverConfig = new ServerConfig()
                .setBaseResource("classpath:/")
                .setClasspath(true);
        new EmbeddedJetty(serverConfig).run();
    }
    
}
