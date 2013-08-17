package org.eluder.jetty.cli.util;

import java.lang.reflect.Method;

import org.eluder.jetty.server.ServerConfig;

public class ServerConfigInitializer {

    protected ServerConfigInitializer() {
        // hide constructor
    }
    
    public static ServerConfig newInstance(final Class<? extends ServerConfig> configType) {
        try {
            ServerConfig serverConfig = configType.newInstance();
            initBooleans(serverConfig);
            return serverConfig;
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static void initBooleans(final ServerConfig serverConfig) {
        for (Method method : ReflectionUtils.getNamedSetters(serverConfig.getClass()).values()) {
            if (PropertyMapper.isBoolean(ReflectionUtils.getSetterArgumentType(method))) {
                ReflectionUtils.invokeMethod(serverConfig, method, Boolean.FALSE);
            }
        }
    }
}
