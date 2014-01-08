package org.eluder.jetty.cli;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eluder.jetty.server.ServerConfig;

public class CliServerContext {
    
    protected static final int DEFAULT_HELP_WIDTH = 100;
    protected static final Class<ServerConfig> CONFIG_TYPE = ServerConfig.class;
    
    protected final String[] args;
    
    public CliServerContext(final String[] args) {
        this.args = args;
    }
    
    protected int getHelpWidth() {
        return DEFAULT_HELP_WIDTH;
    }
    
    protected ServerConfigOptionsFactory createServerConfigOptionsFactory() {
        return new ServerConfigOptionsFactory(CONFIG_TYPE);
    }
    
    protected ServerConfigFactory createServerConfigFactory(final CommandLine command) {
        return new ServerConfigFactory(CONFIG_TYPE, command);
    }
    
    protected CommandLineParser createCommandLineParser() {
        return new BasicParser();
    }
    
    protected Options createOptions() {
        return createServerConfigOptionsFactory().create();
    }
    
    protected EmbeddedJettyCli createServer(final ServerConfig config) {
        return new EmbeddedJettyCli(config);
    }

    protected void showHelp(final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(getHelpWidth());
        formatter.printHelp("java -jar <artifact> <options>", options);
    }
    
    protected void customize(final ServerConfig serverConfig) {
        // noop
    }
    
    protected void init(final Options options) throws Exception {
        try {
            CommandLineParser parser = createCommandLineParser();
            CommandLine command = parser.parse(options, args);
            if (command.hasOption("help")) {
                showHelp(options);
            } else {
                run(command);
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
            showHelp(options);
        }
    }
    
    protected void run(final CommandLine command) throws Exception {
        ServerConfig serverConfig = createServerConfigFactory(command).create();
        customize(serverConfig);
        createServer(serverConfig).run();
    }
    
    public void start() throws Exception {
        Options options = createOptions();
        if (args.length == 0 && !options.getRequiredOptions().isEmpty()) {
            showHelp(options);
        } else {
            init(options);
        }
    }
}
