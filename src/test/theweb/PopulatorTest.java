package theweb;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PopulatorTest {
    public static class Nested {
        public String s;
    }
    
    public static class Base extends Page {
        public Base(String baseUrl) {
            super(baseUrl);
        }

        public String baseProperty;
    }
    
    public static class MockPage extends Base { 
        public MockPage() {
            super("/base/");
        }
        
        public String s;
        public int i;
        public String[] sa;
        public boolean pro;
        
        public Nested nested = new Nested();
        
        public Map<String, Nested> mapNested = new HashMap<String, Nested>();
        public Map<String, String> map = new HashMap<String, String>();
        
        public Map<Integer, Integer> intMap = new HashMap<Integer, Integer>() { };
        
        public void setProperty(String s) {
            this.s = s;
        }
        
        public Nested getPropertyNested() {
            return nested;
        }
    }

    protected Populator populator = new Populator();
    
    private MockPage page = new MockPage();
    private Map<String, Object> props = new HashMap<String, Object>();
    
    public void testSimple() throws Exception {
        props.put("page.s", "a");
        populator.populate(page, props);
        assertEquals("a", page.s);
        
        props.put("page.s", new String[] { "a" });
        populator.populate(page, props);
        assertEquals("a", page.s);
    }
    
    public void testNested() throws Exception {
        props.put("page.nested.s", "a");
        populator.populate(page, props);
        assertEquals("a", page.nested.s);
    }
    
    public void testBooleanTrue() throws Exception {
        props.put("page.pro", "true");

        populator.populate(page, props);

        assertEquals(true, page.pro);
    }
    
    public void testBooleanFalse() throws Exception {
        page.pro = true;
        
        props.put("page.pro", "false");
        
        populator.populate(page, props);
        
        assertEquals(false, page.pro);
    }
    
    public void testMapObjects() throws Exception {
        props.put("page.mapNested[3].s", "value");
        
        page.mapNested.put("3", new Nested());

        populator.populate(page, props);
        
        assertEquals("value", page.mapNested.get("3").s);
    }
    
    public void testMap() throws Exception {
        props.put("page.map[3]", "value");
        
        populator.populate(page, props);
        
        assertEquals("value", page.map.get("3"));
    }
    
    // Only simple bounds like Map<Integer, String> are supported
    public void testIntMap() throws Exception {
        props.put("page.intMap[1]", "2");
        
        populator.populate(page, props);
        
        assertEquals(new Integer(2), page.intMap.get(1));
    }
    
    public void testProperty() throws Exception {
        props.put("page.property", "value");
        
        populator.populate(page, props);
        
        assertEquals("value", page.s);
    }
    
    public void testPropertyNested() throws Exception {
        props.put("page.propertyNested.s", "value");
        
        populator.populate(page, props);
        
        assertEquals("value", page.nested.s);
    }
    
    public void testAccessible() throws Exception {
        props.put("page.a", "v");
        props.put("page.b", "v");
        props.put("page.c", "v");
        props.put("page.d", "v");
        
        populator.populate(TestPage.page, props);
        
        assertEquals("vnullvnull", TestPage.page.toString());
    }
    
    public void testSubclass() throws Exception {
        props.put("page.baseProperty", "v");
        
        populator.populate(page, props);
        
        assertEquals("v", page.baseProperty);
    }
}
