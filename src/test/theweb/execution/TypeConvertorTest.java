package theweb.execution;

import java.util.Arrays;

import junit.framework.TestCase;

public class TypeConvertorTest extends TestCase {

    private TypeConvertor convertor;

    @Override
    protected void setUp() throws Exception {
        convertor = new TypeConvertor();
    }
    
    public void testConvertValue() {
        assertEquals("s", convertor.convertValue("s", String.class));
        assertEquals(5, convertor.convertValue("5", Integer.class));
        assertEquals(null, convertor.convertValue("5", Thread.class));
    }
    
    public void testConvertArray() {
        assertEquals("s", convertor.convertValue(new String[] { "s" }, String.class));
        assertArrayEquals(new String[] { "s" }, (Object[]) convertor.convertValue("s", new String[0].getClass()));
    }
    
    public void testIntString() {
        assertEquals("10", convertor.convertValue(10, String.class));
        assertEquals(10, convertor.convertValue("10", Integer.class));
    }
    
    public void testLongString(){
        assertEquals("10", convertor.convertValue(10L, String.class));
        assertEquals(10L, convertor.convertValue("10", Long.class));        
    }
    
    public void testBooleanString() {
        assertEquals("true", convertor.convertValue(true, String.class));
        assertEquals(true, convertor.convertValue("true", Boolean.class));
        assertEquals(false, convertor.convertValue("false", Boolean.class));
        assertEquals(false, convertor.convertValue("asdasd", Boolean.class));
    }
    
    public enum AA { a, b };
    
    public void testEnum() throws Exception {
        assertEquals(AA.a, convertor.convertValue("a", AA.class));
    }
    
    public void testArrayAndElementConversion() throws Exception {
        assertEquals(true, convertor.convertValue(new String[] { "true" }, Boolean.class));
        assertArrayEquals(new String[] { "true" }, (String[]) convertor.convertValue(true, new String[0].getClass()));
        assertArrayEquals(new String[] { "true" }, (String[]) convertor.convertValue(new boolean[] { true }, new String[0].getClass()));
    }
    
    private static void assertArrayEquals(Object[] l, Object[] object) {
        assertEquals(Arrays.toString(l), Arrays.toString(object));
    }
    
    public void testFloatingPoint() throws Exception {
        assertEquals(1.5, convertor.convertValue("1.5", Double.class));
        assertEquals(1.5f, convertor.convertValue("1.5", Float.class));
        
        assertEquals("1.5", convertor.convertValue(1.5, String.class));
        assertEquals("1.5", convertor.convertValue(1.5f, String.class));
        
        assertEquals("1.5", convertor.convertValue(new Double[] { 1.5 }, String.class));
    }
}
