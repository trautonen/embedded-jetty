package org.eluder.jetty.server.configuration;

import java.io.IOException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eluder.jetty.server.ContextAttributes;

/**
 * Configuration for classpath resources. Adds all classpath resources to
 * containter. Requires at least Jetty 9.0.1 that supports any regexp pattern
 * for included resources.
 */
public class ClasspathConfiguration extends WebInfConfiguration {

    private static final Logger LOG = Log.getLogger(WebInfConfiguration.class);
    
    @Override
    public final void preConfigure(final WebAppContext context) throws Exception {
        String classpathPattern = applyPattern(context);
        if (classpathPattern == null) {
            throw new IllegalArgumentException("Classpath pattern not defined");
        }
        String containerPattern = (String) context.getAttribute(CONTAINER_JAR_PATTERN);
        if (containerPattern == null) {
            containerPattern = classpathPattern;
        } else {
            containerPattern = classpathPattern + "," + containerPattern;
        }
        context.setAttribute(CONTAINER_JAR_PATTERN, containerPattern);
        LOG.debug("Set container JAR pattern to: {}", containerPattern);
        super.preConfigure(context);
    }

    @Override
    public void resolveTempDirectory(final WebAppContext context) throws Exception {
        if (hasStaticContent(context)) {
            super.resolveTempDirectory(context);
        }
    }

    @Override
    public void unpack(final WebAppContext context) throws IOException {
        if (hasStaticContent(context)) {
            super.unpack(context);
        }
    }

    @Override
    public void deconfigure(final WebAppContext context) throws Exception {
        if (hasStaticContent(context)) {
            super.deconfigure(context);
        }
    }

    protected final boolean hasStaticContent(final WebAppContext context) {
        return (context.getWar() != null || context.getBaseResource() != null);
    }

    protected String applyPattern(final WebAppContext context) {
        String pattern = (String) context.getAttribute(ContextAttributes.CLASSPATH_PATTERN);
        return (pattern != null ? pattern : ContextAttributes.ClasspathPatterns.NON_SYSTEM);
    }
}
