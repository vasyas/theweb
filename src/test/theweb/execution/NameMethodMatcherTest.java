package theweb.execution;

import java.lang.reflect.Method;
import java.util.HashMap;

import junit.framework.TestCase;
import theweb.AbstractPage;
import theweb.Action;
import theweb.MockHttpExchange;

@SuppressWarnings("unused")
public class NameMethodMatcherTest extends TestCase {
    public void testNameMethodSlash() throws Exception {
        NameMethodMatcher matcher = new NameMethodMatcher();
        
        Method method = matcher.getMethod(new AbstractPage("/package/page/") { 
            public void method() { }
        }, new MockHttpExchange("/package/page/method"), 
        new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
        
        method = matcher.getMethod(new AbstractPage("/package/page/") { 
            public void method() { }
        }, new MockHttpExchange("/package/page/method/"), new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
    }
    
    public void testActionName() {
        NameMethodMatcher matcher = new NameMethodMatcher();
        
        Method method = matcher.getMethod(new AbstractPage("/package/page/") {
            @Action("method")
            public void qqq() {
                
            }
        }, new MockHttpExchange("/package/page/method"), new HashMap<String, Object>());
        
        assertEquals("qqq", method.getName());
    }
}
