package theweb;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class PathPatternTest extends TestCase {
    public void testCollectIdentity() throws Exception {
        assertEquals("123", new PathPattern("/funds/{id}").matches("/funds/123/action").get("id"));
    }
    
    public void testCollectEmptyIdentity() throws Exception {
        assertNull(new PathPattern("/funds/{id}").matches("/funds/").get("id"));
    }
    
    public void testDontMatch() throws Exception {
        assertNull(new PathPattern("/funds2/{id}").matches("/funds/123/action"));
    }
    
    public void testCreate() throws Exception {
        Map<String, String[]> properties = new HashMap<String, String[]>();
        
        properties.put("a", new String[] { "b" });
        properties.put("c", new String[] { "d" });
        
        assertEquals("/funds/b/", new PathPattern("/funds/{a}/").createPath(properties));
        
        assertEquals(1, properties.size());
        assertEquals("d", properties.get("c")[0]);
    }
    
    public void testCreateNullProperty() throws Exception {
        Map<String, String[]> properties = new HashMap<String, String[]>();
        
        properties.put("a", null);
        properties.put("b", null);
        properties.put("c", null);
        properties.put("d", null);
        
        assertEquals("/funds/", new PathPattern("/funds/{a}/").createPath(properties));
        
        assertFalse(properties.containsKey("a"));
        
        assertEquals("/funds/", new PathPattern("/funds/{b}/{c}").createPath(properties));
        assertEquals("/funds/", new PathPattern("/funds/{d}/aa/").createPath(properties));
    }
    
    public void testCreateMultiValue() throws Exception {
        Map<String, String[]> properties = new HashMap<String, String[]>();
        
        properties.put("a", new String[] { "b", "c" });
        
        try {
            new PathPattern("/funds/{a}/").createPath(properties);
            fail();
        } catch(RuntimeException e) {
        }
    }
    
    public void testMatchingBug() throws Exception {
        PathPattern pattern = new PathPattern("/report/something/");
        
        assertNull(pattern.matches("/report/"));
    }
}
