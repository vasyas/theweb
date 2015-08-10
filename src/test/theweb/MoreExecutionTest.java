package theweb;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoreExecutionTest {
    
    boolean executed;
    
    public class Page2 extends Page {
        public Page2() {
            super("/page/");
        }

        @Default
        public Response execute() {
            executed = true;
            
            return new NoResponse();
        }

        public void anotherExecute() {
        }
    }
    
    private List<MethodMatcher> defaultMatchers() {
    	return Arrays.asList(new NameMethodMatcher(), new DefaultMethodMatcher());
    }

    @Test
    public void testDefaultAction() throws Exception {
        Pages executor = new Pages();
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/"));
        assertTrue(executed);
    }

    @Test
    public void testDefaultActionNoMatchingMethod() throws Exception {
        Pages executor = new Pages();
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/page/asdfasdf"));
        assertTrue(executed);
    }

    @Test
    public void testDefaultActionExistingMethod() throws Exception {
        Pages executor = new Pages();
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/page/anotherExecute"));
        assertFalse(executed);
    }
    
    public class Page3 extends Page2 {
        public boolean executed;
        
        public Response execute(String haha) {
            this.executed = true;
            
            return new NoResponse();
        }
        
        @Default
        public Response executezzz(String haha) {
            this.executed = true;
            
            return new NoResponse();
        }
    }

    @Test
    public void testSameMethodInBaseSuperClass() throws Exception {
        Pages executor = new Pages();
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), new MockHttpExchange("/page/execute"));
        
        assertFalse(executed);
        assertTrue(page.executed);
    }

    @Test
    public void testDefaultActionInBaseSuperClass() throws Exception {
        Pages executor = new Pages();
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), new MockHttpExchange("/page/execute2"));
        
        assertFalse(executed);
        assertTrue(page.executed);
    }
}
