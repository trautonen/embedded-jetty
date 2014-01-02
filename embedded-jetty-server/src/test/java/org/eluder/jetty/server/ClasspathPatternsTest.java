package org.eluder.jetty.server;

import static org.junit.Assert.fail;

import java.net.URI;
import java.util.regex.Pattern;

import org.eclipse.jetty.util.PatternMatcher;
import org.junit.Test;

public class ClasspathPatternsTest {
    
    @Test
    public void testNonSystem() throws Exception {
        final String pattern = ContextAttributes.ClasspathPatterns.NON_SYSTEM;
        assertMatch(pattern, "/path/to/classes/");
        assertMatch(pattern, "/path/to/jarjar.jar");
        assertNotMatch(pattern, "/usr/lib/jvm/jre/lib/");
        assertNotMatch(pattern, "/.m2/repository/org/eclipse/jetty/jetty-server.jar");
    }

    @Test
    public void testAllJars() throws Exception {
        final String pattern = ContextAttributes.ClasspathPatterns.ALL_JARS;
        assertMatch(pattern, "/path/to/jarjar.jar");
        assertNotMatch(pattern, "/path/to/classes/");
        assertNotMatch(pattern, "/path/to/generated-sources/");
    }
    
    @Test
    public void testNonJars() throws  Exception{
        final String pattern = ContextAttributes.ClasspathPatterns.NON_JARS;
        assertMatch(pattern, "/path/to/classes/");
        assertMatch(pattern, "/path/to/generated-sources/");
        assertNotMatch(pattern, "/path/to/jarjar.jar");
    }
    
    @Test
    public void testClasses() throws Exception {
        final String pattern = ContextAttributes.ClasspathPatterns.CLASSES;
        assertMatch(pattern, "/path/to/classes/");
        assertMatch(pattern, "/path/to/test-classes/");
        assertNotMatch(pattern, "/path/to/jarjar.jar");
        assertNotMatch(pattern, "/path/to/generated-sources/");
    }
    
    private static void assertMatch(String pattern, String uri) throws Exception {
        if (!match(pattern, uri)) {
            fail(uri + " does not match pattern " + pattern);
        }
    }
    
    private static void assertNotMatch(String pattern, String uri) throws Exception {
        if (match(pattern, uri)) {
            fail(uri + " matches pattern " + pattern);
        }
    }
    
    private static boolean match(String pattern, String uri) throws Exception {
        final boolean[] match = new boolean[] { false };
        new PatternMatcher() {
            @Override
            public void matched(final URI uri) throws Exception {
                match[0] = true;
            }
        }.match(Pattern.compile(pattern), new URI[] { new URI(uri) }, false);
        return match[0];
    }
}
