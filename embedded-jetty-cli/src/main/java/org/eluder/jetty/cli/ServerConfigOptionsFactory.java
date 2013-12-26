package org.eluder.jetty.cli;

import java.lang.reflect.Method;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.eclipse.jetty.util.annotation.Name;
import org.eluder.jetty.cli.util.PropertyMapper;
import org.eluder.jetty.cli.util.ReflectionUtils;
import org.eluder.jetty.cli.util.ServerConfigInitializer;
import org.eluder.jetty.server.ServerConfig;

/**
 * Factory to create command line options reflectively from server config.
 */
public class ServerConfigOptionsFactory {
    
    private static final int SETTER_PREFIX_LENGTH = 3;
    
    private final Class<? extends ServerConfig> configType;
    
    public ServerConfigOptionsFactory(final Class<? extends ServerConfig> configType) {
        this.configType = configType;
    }

    public Options create() {
        Options options = new Options();
        ServerConfig initial = ServerConfigInitializer.newInstance(configType);
        for (Method method : ReflectionUtils.getNamedSetters(configType).values()) {
            Name name = ReflectionUtils.getName(method);
            Class<?> argumentType = ReflectionUtils.getSetterArgumentType(method);
            Object defaultValue = ReflectionUtils.invokeMethod(initial, toGetter(method));
            @SuppressWarnings("static-access")
            Option option = OptionBuilder
                    .withArgName(getArgumentName(argumentType))
                    .hasArg(!PropertyMapper.isBoolean(argumentType))
                    .withDescription(getDescription(name.description(), defaultValue))
                    .isRequired(isRequired(name.value()))
                    .create(name.value());
            options.addOption(option);
        }
        return options;
    }
    
    private String getArgumentName(final Class<?> argumentType) {
        if (PropertyMapper.isInteger(argumentType)) {
            return "number";
        }
        if (PropertyMapper.isBoolean(argumentType)) {
            return "true|false";
        }
        if (PropertyMapper.isString(argumentType)) {
            return "value";
        }
        return "";
    }
    
    private boolean isRequired(final String opt) {
        if ("webApp".equals(opt)) {
            return true;
        }
        return false;
    }
    
    private String getDescription(final String description, final Object defaultValue) {
        if (defaultValue == null) {
            return description;
        } else {
            return description + " (default " + defaultValue.toString() + ")";
        }
    }
    
    private Method toGetter(final Method setter) {
        try {
            Class<?> target = setter.getDeclaringClass();
            Class<?> argumentType = ReflectionUtils.getSetterArgumentType(setter);
            String getterName = (PropertyMapper.isBoolean(argumentType) ? "is" : "get") + setter.getName().substring(SETTER_PREFIX_LENGTH);
            return target.getMethod(getterName);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
