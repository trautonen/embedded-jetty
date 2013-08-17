package org.eluder.jetty.cli.util;

import static org.eluder.jetty.cli.util.PropertyMapper.getMappedValue;
import static org.eluder.jetty.cli.util.PropertyMapper.isBoolean;
import static org.eluder.jetty.cli.util.PropertyMapper.isInteger;
import static org.eluder.jetty.cli.util.PropertyMapper.isString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.Test;

public class PropertyMapperTest {

    @Test
    public void testGetMappedString() throws Exception {
        assertEquals("test", getMappedValue(String.class, "test"));
    }
    
    @Test
    public void testGetMappedInteger() throws Exception {
        assertEquals(10, getMappedValue(Integer.class, "10"));
        assertEquals(5, getMappedValue(int.class, "5"));
    }
    
    @Test
    public void testGetMappedBoolean() throws Exception {
        assertEquals(true, getMappedValue(Boolean.class, "true"));
        assertEquals(false, getMappedValue(boolean.class, "false"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetUnsupportedValue() throws Exception {
        getMappedValue(Date.class, "2010-10-11");
    }
    
    @Test
    public void testIsString() {
        assertTrue(isString(String.class));
        assertFalse(isString(Integer.class));
    }
    
    @Test
    public void testIsInteger() {
        assertTrue(isInteger(Integer.class));
        assertTrue(isInteger(int.class));
        assertFalse(isInteger(Long.class));
    }
    
    @Test
    public void testIsBoolean() {
        assertTrue(isBoolean(Boolean.class));
        assertTrue(isBoolean(boolean.class));
        assertFalse(isBoolean(Number.class));
    }

}
