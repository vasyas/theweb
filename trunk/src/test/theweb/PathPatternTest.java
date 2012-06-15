package theweb;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class PathPatternTest extends TestCase {
    public void testCollectIdentity() throws Exception {
        assertEquals("123", new PathPattern("/funds/{id}/").match("/funds/123/action").get("id"));
    }
    
    public void testCollectEmptyIdentity() throws Exception {
        assertNull(new PathPattern("/funds/{id}/").match("/funds/").get("id"));
        
        assertEquals("", new PathPattern("/funds/{id}/test/").match("/funds//test").get("id"));
        assertEquals("", new PathPattern("/funds/{id}/test/").match("/funds//test/").get("id"));
    }
    
    public void testDontMatch() throws Exception {
        assertFalse(new PathPattern("/funds2/{id}/").match("/funds/123/action").matched());
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
        
        assertEquals("/funds/", new PathPattern("/funds/{b}/{c}/").createPath(properties));
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
        
        assertFalse(pattern.match("/report/").matched());
    }
    
    public void testRemaining() throws Exception {
        assertEquals("test", new PathPattern("/report/").match("/report/test").remaining);
        assertEquals("test/aa", new PathPattern("/report/").match("/report/test/aa").remaining);
        
        assertEquals("aa", new PathPattern("/report/{var}/").match("/report/test/aa").remaining);
        assertEquals("aa/", new PathPattern("/report/{var}/").match("/report/test/aa/").remaining);
        assertEquals("aa/", new PathPattern("/report/{var}/").match("/report//aa/").remaining);
        assertEquals("remaining", new PathPattern("/report/{var}/test/").match("/report/var/test/remaining").remaining);
    }
    
    public void testRemainingBug() throws Exception {
        assertEquals("method", new PathPattern("/report/").match("/report/method").remaining);
        assertEquals("method", new PathPattern("/").match("/method").remaining);
    }
    
    public void testSlashBug() throws Exception {
        assertTrue(new PathPattern("/").match("/").matched());
	}
}
