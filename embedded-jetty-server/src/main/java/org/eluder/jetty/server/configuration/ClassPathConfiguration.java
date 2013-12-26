package org.eluder.jetty.server.configuration;

import java.io.IOException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

/**
 * Configuration for classpath resources. Adds /test-classes and /classes
 * directories to included pattern. Requires at least Jetty 9.0.1 that supports
 * any regexp pattern for included resources.
 */
public class ClassPathConfiguration extends WebInfConfiguration {

    private static final Logger LOG = Log.getLogger(WebInfConfiguration.class);
    
    private static final String CLASSES_PATTERN = ".*/test-classes/.*,.*/classes/.*";
    
    @Override
    public void preConfigure(final WebAppContext context) throws Exception {
        String containerPattern = applyPatterns(context);
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

    protected String applyPatterns(final WebAppContext context) throws Exception {
        String containerPattern = (String) context.getAttribute(CONTAINER_JAR_PATTERN);
        if (containerPattern == null) {
            return CLASSES_PATTERN;
        } else {
            return CLASSES_PATTERN + "," + containerPattern;
        }
    }
    
    protected final boolean hasStaticContent(final WebAppContext context) {
        return (context.getWar() != null || context.getBaseResource() != null);
    }
}
