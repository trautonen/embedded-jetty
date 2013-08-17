package org.eluder.jetty.cli.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.annotation.Name;
import org.eluder.jetty.server.ServerConfig;

public class ReflectionUtils {

    protected ReflectionUtils() {
        // hide constructor
    }
    
    public static Map<String, Method> getNamedSetters(final Class<? extends ServerConfig> configType) {
        Map<String, Method> optionSetMethods = new HashMap<>();
        for (Method method : configType.getMethods()) {
            if (isSetter(method)) {
                Name name = getName(method);
                if (name != null) {
                    optionSetMethods.put(name.value(), method);
                }
            }
        }
        return optionSetMethods;
    }
    
    public static Name getName(final Method method) {
        Annotation[] annotations = method.getParameterAnnotations()[0];
        for (Annotation annotation : annotations) {
            if (annotation instanceof Name) {
                return (Name) annotation;
            }
        }
        return null;
    }
    
    public static Class<?> getSetterArgumentType(final Method method) {
        return method.getParameterTypes()[0];
    }
    
    public static boolean isSetter(final Method method) {
        return (method.getName().startsWith("set") &&
                method.getParameterTypes().length == 1);
    }
    
    public static Object invokeMethod(final Object target, final Method method, final Object... arguments) {
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
