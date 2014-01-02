package org.eluder.jetty.server.configuration;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eluder.jetty.server.ContextAttributes;
import org.eluder.jetty.server.ServerConfig;

/**
 * Configuration for loading JARs. Sets the contexts's extra classpath to
 * include the JAR.
 */
public class JarAppConfiguration extends WebInfConfiguration {

    private static final Logger LOG = Log.getLogger(JarAppConfiguration.class);
    
    @Override
    public final void preConfigure(final WebAppContext context) throws Exception {
        if (isJar(context.getWar())) {
            String jarAppPattern = applyPattern(context);
            if (jarAppPattern == null) {
                throw new IllegalArgumentException("JAR application pattern not defined");
            }
            String extraClasspath = context.getExtraClasspath();
            if (extraClasspath == null) {
                extraClasspath = jarAppPattern;
            } else {
                extraClasspath = jarAppPattern + "," + extraClasspath;
            }
            context.setExtraClasspath(extraClasspath);
            LOG.debug("Set extra classpath to: {}", extraClasspath);
        }
        super.preConfigure(context);
    }

    protected final boolean isJar(final String resource) {
        return (resource != null && resource.endsWith(ServerConfig.JAR_SUFFIX));
    }

    protected String applyPattern(final WebAppContext context) {
        String pattern = (String) context.getAttribute(ContextAttributes.JAR_APP_PATTERN);
        return (pattern != null ? pattern : context.getWar());
    }
}
