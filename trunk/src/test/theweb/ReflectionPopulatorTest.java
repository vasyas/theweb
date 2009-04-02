package theweb;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class ReflectionPopulatorTest extends TestCase {
    public static class Nested {
        public String s;
    }
    
    public static class MockPage extends Page { 
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
        
        public void setProperty(String s) {
            this.s = s;
        }
        
        public Nested getPropertyNested() {
            return nested;
        }
    }

    protected Populator populator = new ReflectionPopulator(true);
    
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
    
    public void testParent() throws Exception {
        populator = new ReflectionPopulator(false);
        
        props.put("s", "a");
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
}
