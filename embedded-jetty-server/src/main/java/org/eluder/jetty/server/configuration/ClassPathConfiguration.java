package org.eluder.jetty.server.configuration;

import java.io.IOException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

public class ClassPathConfiguration extends WebInfConfiguration {

    private static final Logger LOG = Log.getLogger(WebInfConfiguration.class);
    
    private static final String CLASSES_PATTERN = ".*/test-classes/.*,.*/classes/.*";
    
    @Override
    public void preConfigure(WebAppContext context) throws Exception {
        String containerPattern = applyPatterns(context);
        context.setAttribute(CONTAINER_JAR_PATTERN, containerPattern);
        LOG.debug("Set container JAR pattern to: {}", containerPattern);
        super.preConfigure(context);
    }

    @Override
    public void resolveTempDirectory(WebAppContext context) throws Exception {
        if (hasStaticContent(context)) {
            super.resolveTempDirectory(context);
        }
    }

    @Override
    public void unpack(WebAppContext context) throws IOException {
        if (hasStaticContent(context)) {
            super.unpack(context);
        }
    }

    protected String applyPatterns(WebAppContext context) throws Exception {
        String containerPattern = (String) context.getAttribute(CONTAINER_JAR_PATTERN);
        if (containerPattern == null) {
            return CLASSES_PATTERN;
        } else {
            return CLASSES_PATTERN + "," + containerPattern;
        }
    }
    
    protected final boolean hasStaticContent(WebAppContext context) {
        return (context.getWar() != null || context.getBaseResource() != null);
    }
}
