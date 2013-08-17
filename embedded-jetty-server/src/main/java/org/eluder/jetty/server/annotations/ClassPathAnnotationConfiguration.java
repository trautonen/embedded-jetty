package org.eluder.jetty.server.annotations;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.annotations.AbstractDiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.annotations.AnnotationParser.DiscoverableAnnotationHandler;
import org.eclipse.jetty.annotations.ClassNameResolver;
import org.eclipse.jetty.util.PatternMatcher;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class ClassPathAnnotationConfiguration extends AnnotationConfiguration {

    public static final String CLASSPATH_JAR_PATTERN = "org.eclipse.jetty.server.webapp.ClassPathIncludeJarPattern";
    
    private static final Logger LOG = Log.getLogger(ClassPathAnnotationConfiguration.class);
    
    @Override
    public void configure(final WebAppContext context) throws Exception {
        if (isJar(context.getWar())) {
            Resource jar = Resource.newResource(context.getWar());
            String filePattern = ".*" + Pattern.quote(FilenameUtils.getName(jar.getName()));
            String jarPattern = (String) context.getAttribute(CLASSPATH_JAR_PATTERN);
            if (jarPattern == null || jarPattern.isEmpty()) {
                jarPattern = filePattern;
            } else {
                jarPattern += "," + filePattern;
            }
            context.setAttribute(CLASSPATH_JAR_PATTERN, jarPattern);
            ((WebAppClassLoader) context.getClassLoader()).addClassPath(jar);
        }
        super.configure(context);
    }
    
    @Override
    public void parseContainerPath(final WebAppContext context, final AnnotationParser parser) throws Exception {
        URI[] classPathUris = getClassPathUris(context);
        parser.clearHandlers();
        for (DiscoverableAnnotationHandler h : _discoverableAnnotationHandlers) {
            if (h instanceof AbstractDiscoverableAnnotationHandler) {
                ((AbstractDiscoverableAnnotationHandler) h).setResource(null);
            }
        }
        parser.registerHandlers(_discoverableAnnotationHandlers);
        parser.registerHandler(_classInheritanceHandler);
        parser.registerHandlers(_containerInitializerAnnotationHandlers);

        parser.parse(classPathUris, getClassNameResolver(context));
    }

    @Override
    public void parseWebInfClasses(final WebAppContext context, final AnnotationParser parser) throws Exception {
        // noop
    }

    @Override
    public void parseWebInfLib(final WebAppContext context, final AnnotationParser parser) throws Exception {
        // noop
    }

    private URI[] getClassPathUris(final WebAppContext context) throws Exception {
        final List<URI> classPathUris = new ArrayList<>();
        final PatternMatcher jarMatcher = new PatternMatcher() {
            @Override
            public void matched(final URI uri) throws Exception {
                classPathUris.add(uri);
            }
        };
        ClassLoader loader = context.getClassLoader();
        URI[] holder = new URI[1];
        Pattern jarPattern = getJarPattern(context);
        while (loader != null && (loader instanceof URLClassLoader)) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            if (urls != null) {
                for (URL u : urls) {
                    try {
                        holder[0] = u.toURI();
                    } catch (URISyntaxException ex) {
                        holder[0] = new URI(u.toString().replaceAll(" ", "%20"));
                    }
                    if (isJar(holder[0].toString())) {
                        jarMatcher.match(jarPattern, holder, false);
                    } else {
                        classPathUris.add(holder[0]);
                    }
                }
            }
            loader = loader.getParent();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding annotatated classes from: " + classPathUris.toString());
        }
        return classPathUris.toArray(new URI[classPathUris.size()]);
    }
    
    private ClassNameResolver getClassNameResolver(final WebAppContext context) {
        return new ClassNameResolver() {
            @Override
            public boolean isExcluded(final String name) {
                if (context.isSystemClass(name)) return true;
                if (context.isServerClass(name)) return false;
                return false;
            }

            @Override
            public boolean shouldOverride(final String name) {
                // looking at webapp classpath, found already-parsed class of
                // same name - did it come from system or duplicate in webapp?
                if (context.isParentLoaderPriority()) return false;
                return true;
            }
        };
    }
    
    private Pattern getJarPattern(final WebAppContext context) {
        String attribute = (String) context.getAttribute(CLASSPATH_JAR_PATTERN);
        return (attribute == null ? null : Pattern.compile(attribute));
    }
    
    private boolean isJar(final String resource) {
        return (resource != null && resource.endsWith(".jar"));
    }
}
