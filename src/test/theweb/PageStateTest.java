package theweb;

import junit.framework.TestCase;

public class PageStateTest extends TestCase {
    public static class MockPageNone extends Page { 
        public MockPageNone() {
            super("/base/");
        }
    }
    
    public static class MockPageStringFields extends Page { 
        public MockPageStringFields() {
            super("/base/");
        }
        
        public String field1;
        public String field2 = "";
        public String field3 = "value";
    }
    
    public static class MockPagePattern extends Page { 
        public MockPagePattern() {
            super("/base/{page.field1}/");
        }
        
        public String field1 = "1";
        public String field2 = "2";
    }
    
    @Override
    protected void setUp() throws Exception {
        new ContextInfo("/context");
    }
    
    public void testObjectPageState() throws Exception {
        assertEquals("/context/base/", new PageState(new MockPageNone()).view());
        assertEquals("/context/base/test", new PageState(new MockPageNone()).action("test"));
        
        assertEquals("/context/base/test?page.field2=&page.field3=value", new PageState(new MockPageStringFields()).action("test"));
    }
    
    public void testPathPattern() throws Exception {
        assertEquals("/context/base/1/?page.field2=2", new PageState(new MockPagePattern()).view());
    }
}
