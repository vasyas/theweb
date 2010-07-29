package theweb.velocity;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class FieldAwareUberspectTest extends TestCase {
    @SuppressWarnings("unused")
    public void testMethodInANonVisibleClass() throws Exception {
        VelocityTemplate t = new VelocityTemplate(FieldAwareUberspectTest.class, "method.vm");
        
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("object", new Object() { 
            public String method() {
                return "AAA";
            }
        });
        
        assertEquals("AAA", t.render(context));
    }
}
