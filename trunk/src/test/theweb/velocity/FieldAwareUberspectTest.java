package theweb.velocity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldAwareUberspectTest  {
    @Test
    public void testMethodInANonVisibleClass() throws Exception {
        Template t = new Template(FieldAwareUberspectTest.class, "method.vm");
        
        t.context("object", new Object() {
            public String method() {
                return "AAA";
            }
        });

        assertEquals("AAA", t.render());
    }
}
