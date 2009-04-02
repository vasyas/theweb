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
    
    private static void assertArrayEquals(Object[] l, Object[] object) {
        assertEquals(Arrays.toString(l), Arrays.toString(object));
    }
}
