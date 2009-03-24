package theweb.execution;

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

}
