package theweb;

import theweb.ContextInfo;
import theweb.Markup;
import theweb.Page;
import theweb.PageState;
import junit.framework.TestCase;

public class PageStateTest extends TestCase {
    public static class MockPage extends Page {
        public MockPage() {
            super("/base/");
        }
        
        @Override
        public Markup markup() {
            return null;
        }
    }
    
    public static class MockPageNone extends MockPage { }
    public static class MockPageStringFields extends MockPage { 
        public String field1;
        public String field2 = "";
        public String field3 = "value";
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
}
