package theweb.execution;

import java.lang.reflect.Method;
import java.util.HashMap;

import junit.framework.TestCase;
import theweb.AbstractPage;
import theweb.MockHttpExchange;

public class HttpMethodMatcherTest extends TestCase {

    @SuppressWarnings("unused")
    public void testGetMethod() {
        HttpMethodMatcher m = new HttpMethodMatcher();
        
        Method method = m.getMethod(new AbstractPage("/") {
            @HttpMethod("POST")
            public void m1() {
                
            }
        }, new MockHttpExchange("/") { 
            @Override
            public String getRequestMethod() {
                return "POST";
            }
        }, new HashMap<String, Object>());
        
        assertEquals("m1", method.getName());
    }

}
