package theweb;

import junit.framework.TestCase;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

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
    
    public static class MockPageTypes extends Page {
        public MockPageTypes() {
            super("/base/");
        }
        
        public boolean field1;
    }
    
    public static class MockPageTransient extends Page {
        public MockPageTransient() {
            super("/base/");
        }
        
        public String a = "b";
        public transient String c = "b";
        public final String e = "b";
        public static String f = "b";
    }
    
    public void testObjectPageState() throws Exception {
        assertEquals("/base/", new PageState(new MockPageNone()).view());
        assertEquals("/base/test", new PageState(new MockPageNone()).action("test"));
        
        assertEquals("/base/test?page.field3=value", new PageState(new MockPageStringFields()).action("test"));
    }
    
    public void testPathPattern() throws Exception {
        assertEquals("/base/1/?page.field2=2", new PageState(new MockPagePattern()).view());
    }
    
    public void testTypeConversion() throws Exception {
        assertEquals("/base/?page.field1=false", new PageState(new MockPageTypes()).view());
    }
    
    public void testTransientProperties() throws Exception {
        assertEquals("/base/?page.a=b", new PageState(new MockPageTransient()).view());
    }

    @SuppressWarnings("unused")
    Page p1 = new MockPageNone() {
        public String a = "b";
    };
    
    @SuppressWarnings("unused")
    private static class InaccessiblePage extends MockPageNone {
        public String a = "b";
    }
    
    public void testAccessible() throws Exception {
        assertEquals("/base/?page.a=b", new PageState(p1).view());
        assertEquals("/base/?page.a=b", new PageState(new InaccessiblePage()).view());
    }
    
    public static class BaseCustomDescriptionPage extends MockPageNone {
        @CustomDescription
        public void describeBase(Map<String, String[]> parameterMap) {
            parameterMap.put("base", new String[] { "a" });
        }
    }
    
    public static class CustomDescriptionPage extends BaseCustomDescriptionPage {
        @CustomDescription
        public void describe(Map<String, String[]> parameterMap) {
            parameterMap.put("derived", new String[] { "b" });
        }
    }
    
    public void testCustomDescription() throws Exception {
        assertEquals("/base/?base=a&derived=b", new PageState(new CustomDescriptionPage()).view());
    }
    
    static class EnumStatePage extends Page {

        public EnumStatePage() {
            super("/");
        }

        public Item item = Item.apple;
        
        public String getLink(){
            return new PageState(this).action("somelink");
        }
       
        enum Item { apple, orange, lemon }
    }
    
    public void testEnum() throws Exception {
        assertEquals("/?page.item=apple", new PageState(new EnumStatePage()).view());
    }
    
    public void testEncoding() throws Exception {
        MockPageStringFields page = new MockPageStringFields();
        page.field2 = "&";
        
        assertEquals("/base/test?page.field2=" + URLEncoder.encode("&", "UTF-8")+ "&page.field3=value", new PageState(page).action("test"));
    }

    static class Inner {
        public String a = "b";
    }
    
    static class MockOuterPage extends Page {
        public MockOuterPage() {
            super("/base/");
        }
        
        @Describe
        public Inner inner;
    }
    
    public void testDescribeNull() throws Exception {
        MockOuterPage page = new MockOuterPage();
        
        assertEquals("/base/test", new PageState(page).action("test"));
    }

    public Page pageWithMap = new MockPageNone() {
        public Map<String, String> values = new LinkedHashMap<String, String>(); {
            values.put("k1", "v1");
            values.put("k2", "v2");
        };
    };

    public void testDescribeMap() throws Exception {
        assertEquals("/base/?page.values[k1]=v1&page.values[k2]=v2", new PageState(pageWithMap).view());
    }
}
