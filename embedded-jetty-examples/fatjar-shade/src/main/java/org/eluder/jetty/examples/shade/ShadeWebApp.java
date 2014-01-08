package org.eluder.jetty.examples.shade;

import java.lang.reflect.Method;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.eluder.jetty.cli.CliServerContext;
import org.eluder.jetty.cli.ServerConfigOptionsFactory;
import org.eluder.jetty.cli.util.ReflectionUtils;
import org.eluder.jetty.server.ServerConfig;

public class ShadeWebApp {

    private static final Set<String> IGNORED_OPTIONS = ImmutableSet.of(
            "webApp", "baseResource", "plus", "classpath"
    );

    protected ShadeWebApp() { }
    
    public static void main(final String[] args) throws Exception {
        CliServerContext context = new CliServerContext(args) {
            @Override
            protected void customize(final ServerConfig serverConfig) {
                serverConfig.setClasspath(true);
            }

            @Override
            protected ServerConfigOptionsFactory createServerConfigOptionsFactory() {
                return new ServerConfigOptionsFactory(CONFIG_TYPE) {
                    @Override
                    protected Iterable<Method> getNamedSetters() {
                        return Iterables.filter(super.getNamedSetters(), new Predicate<Method>() {
                            @Override
                            public boolean apply(final Method input) {
                                String name = ReflectionUtils.getName(input).value();
                                return !IGNORED_OPTIONS.contains(name);
                            }
                        });
                    }
                };
            }
        };
        context.start();
    }
}
