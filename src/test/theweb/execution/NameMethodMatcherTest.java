package theweb.execution;

import java.lang.reflect.Method;
import java.util.HashMap;

import junit.framework.TestCase;
import theweb.AbstractPage;
import theweb.MockHttpServletRequest;

public class NameMethodMatcherTest extends TestCase {
    @SuppressWarnings("unused")
    public void testNameMethodSlash() throws Exception {
        NameMethodMatcher matcher = new NameMethodMatcher();
        
        Method method = matcher.getMethod(new AbstractPage("/package/page/") { 
            public void method() { }
        }, new MockHttpServletRequest() { 
            @Override
            public String getPathInfo() {
                return "/package/page/method";
            }
            
            @Override
            public String getServletPath() {
                return "";
            }
        }, new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
        
        method = matcher.getMethod(new AbstractPage("/package/page/") { 
            public void method() { }
        }, new MockHttpServletRequest() { 
            @Override
            public String getPathInfo() {
                return "/package/page/method/";
            }
            
            @Override
            public String getServletPath() {
                return "";
            }
        }, new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
    }
}
