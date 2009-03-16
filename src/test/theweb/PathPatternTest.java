package theweb;

import junit.framework.TestCase;

public class PathPatternTest extends TestCase {
    public void testCollectIdentity() throws Exception {
        assertEquals("123", new PathPattern("/funds/{id}").parse("/funds/123/action").get("id"));
    }
    
    public void testCollectEmptyIdentity() throws Exception {
        assertNull(new PathPattern("/funds/{id}").parse("/funds/").get("id"));
    }
    
    public void testDontMatch() throws Exception {
        assertEquals("123", new PathPattern("/funds2/{id}").parse("/funds/123/action").get("id"));
    }
}
