package org.eluder.jetty.server.configuration;

import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eluder.jetty.server.ServerConfig;

/**
 * Configuration for loading JARs. Sets the contexts's extra classpath to
 * include the JAR.
 */
public class JarAppConfiguration extends WebInfConfiguration {

    @Override
    public void preConfigure(final WebAppContext context) throws Exception {
        if (isJar(context.getWar())) {
            String extraClasspath = context.getExtraClasspath();
            if (extraClasspath == null) {
                extraClasspath = context.getWar();
            } else {
                extraClasspath = context.getWar() + "," + extraClasspath;
            }
            context.setExtraClasspath(extraClasspath);
        }
        super.preConfigure(context);
    }

    private boolean isJar(final String resource) {
        return (resource != null && resource.endsWith(ServerConfig.JAR_SUFFIX));
    }
}
