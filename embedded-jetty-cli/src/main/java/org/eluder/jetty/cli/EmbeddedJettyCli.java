package org.eluder.jetty.cli;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eluder.jetty.server.EmbeddedJetty;
import org.eluder.jetty.server.ServerConfig;

/**
 * Command line interface for embedded Jetty.
 */
public final class EmbeddedJettyCli extends EmbeddedJetty {
    
    private static final int HELP_WIDTH = 100;
    private static final Class<? extends ServerConfig> CONFIG_TYPE = ServerConfig.class;
    
    private EmbeddedJettyCli(final ServerConfig serverConfig) {
        super(serverConfig);
    }
    
    @Override
    protected void start(final Server server) throws Exception {
        server.start();
        server.join();
    }
    
    public static void main(final String[] args) throws Exception {
        Options options = new ServerConfigOptionsFactory(CONFIG_TYPE).create();
        if (args.length == 0) {
            showHelp(options);
        } else {
            run(args, options);
        }
    }
    
    private static void run(final String[] args, final Options options) throws Exception {
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine command = parser.parse(options, args);
            ServerConfig serverConfig = new ServerConfigFactory(CONFIG_TYPE, command).create();
            new EmbeddedJettyCli(serverConfig).run();
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
            showHelp(options);
        }
    }
    
    private static void showHelp(final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(HELP_WIDTH);
        formatter.printHelp("java -jar <artifact> <options>", options);
    }
}
