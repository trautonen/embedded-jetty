package org.eluder.jetty.server.annotations;

import java.util.regex.Pattern;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class JarAppAnnotationConfiguration extends ClassPathAnnotationConfiguration {

    private static final char UNIX_DIR_SEPARATOR = '/';
    private static final char WINDOWS_DIR_SEPARATOR = '\\';
    
    @Override
    public void configure(final WebAppContext context) throws Exception {
        if (isJar(context.getWar())) {
            Resource jar = Resource.newResource(context.getWar());
            String filePattern = ".*" + Pattern.quote(getFileName(jar.getName()));
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
    
    private String getFileName(final String path) {
        int lastUnixPos = path.lastIndexOf(UNIX_DIR_SEPARATOR);
        int lastWindowsPos = path.lastIndexOf(WINDOWS_DIR_SEPARATOR);
        int index = Math.max(lastUnixPos, lastWindowsPos);
        return path.substring(index + 1);
    }
}
