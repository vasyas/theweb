package theweb;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.HashMap;

@SuppressWarnings("unused")
public class NameMethodMatcherTest extends TestCase {
    public void testNameMethodSlash() throws Exception {
        NameMethodMatcher matcher = new NameMethodMatcher();
        
        Method method = matcher.getMethod(new Page("/package/page/") {
            public void method() { }
        }, new MockHttpExchange("/package/page/method"), 
        new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
        
        method = matcher.getMethod(new Page("/package/page/") {
            public void method() { }
        }, new MockHttpExchange("/package/page/method/"), new HashMap<String, Object>());
        
        assertEquals("method", method.getName());
    }
    
    public void testActionName() {
        NameMethodMatcher matcher = new NameMethodMatcher();
        
        Method method = matcher.getMethod(new Page("/package/page/") {
            @Action("method")
            public void qqq() {
                
            }
        }, new MockHttpExchange("/package/page/method"), new HashMap<String, Object>());
        
        assertEquals("qqq", method.getName());
    }
}
