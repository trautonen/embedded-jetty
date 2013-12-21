package org.eluder.jetty.server.configuration;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

public class JarAppConfiguration extends WebInfConfiguration {

    @Override
    public void preConfigure(WebAppContext context) throws Exception {
        if (isJar(context.getWar())) {
            Resource jar = Resource.newResource(context.getWar());
            ((WebAppClassLoader) context.getClassLoader()).addJars(jar);
            
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
        return (resource != null && resource.endsWith(".jar"));
    }
}
