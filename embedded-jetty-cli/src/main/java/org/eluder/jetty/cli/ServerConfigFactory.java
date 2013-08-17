package org.eluder.jetty.cli;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.eluder.jetty.cli.util.PropertyMapper;
import org.eluder.jetty.cli.util.ReflectionUtils;
import org.eluder.jetty.cli.util.ServerConfigInitializer;
import org.eluder.jetty.server.ServerConfig;

public class ServerConfigFactory {

    private final Class<? extends ServerConfig> configType;
    private final CommandLine command;
    
    public ServerConfigFactory(final Class<? extends ServerConfig> configType, final CommandLine command) {
        this.configType = configType;
        this.command = command;
    }

    public ServerConfig create() {
        Map<String, Method> methods = ReflectionUtils.getNamedSetters(configType);
        ServerConfig serverConfig = ServerConfigInitializer.newInstance(configType);
        for (Option option : command.getOptions()) {
            Method method = methods.get(option.getOpt());
            ReflectionUtils.invokeMethod(serverConfig, method, getArgument(method, option.getValue()));
        }
        return serverConfig;
    }
    
    private Object getArgument(final Method method, final String value) {
        Class<?> argumentType = ReflectionUtils.getSetterArgumentType(method);
        if (PropertyMapper.isBoolean(argumentType)) {
            return Boolean.TRUE;
        } else {
            return PropertyMapper.getMappedValue(argumentType, value);
        }
    }
}
