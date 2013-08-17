package org.eluder.jetty.cli.util;

public class PropertyMapper {

    protected PropertyMapper() {
        // hide constructor
    }
    
    public static Object getMappedValue(final Class<?> type, final String value) {
        if (isString(type)) {
            return value;
        }
        if (isInteger(type)) {
            return Integer.valueOf(value);
        }
        if (isBoolean(type)) {
            return Boolean.valueOf(value);
        }
        throw new IllegalArgumentException("Mapping of " + type.getSimpleName() + " is not supported");
    }
    
    public static boolean isString(final Class<?> type) {
        return String.class.equals(type);
    }
    
    public static boolean isInteger(final Class<?> type) {
        return Integer.class.equals(type) || int.class.equals(type);
    }
    
    public static boolean isBoolean(final Class<?> type) {
        return Boolean.class.equals(type) || boolean.class.equals(type);
    }
}
