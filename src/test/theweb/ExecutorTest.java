package theweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import theweb.execution.DefaultActionMethodMatcher;
import theweb.execution.Executor;
import theweb.execution.MethodMatcher;
import theweb.execution.NameMethodMatcher;
import theweb.execution.PageInterceptor;

public class ExecutorTest extends TestCase {
    
    boolean executed;
    
    public class Page2 extends AbstractPage {
        public Page2() {
            super("/");
        }

        @DefaultAction
        public Outcome execute() {
            executed = true;
            
            return new RenderOutcome();
        }
    }
    
    private List<MethodMatcher> defaultMatchers() {
    	return Arrays.asList(new NameMethodMatcher(), new DefaultActionMethodMatcher());
    }
    
    public void testDefaultAction() throws Exception {
        Executor executor = new Executor(defaultMatchers(), new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/";
        
        executor.exec(new Page2(), new HashMap<String, Object>(), r, null);
        assertTrue(executed);
    }
    
    public void testDefaultActionNoMatchingMethod() throws Exception {
        Executor executor = new Executor(defaultMatchers(), new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/asdfasdf";
        
        executor.exec(new Page2(), new HashMap<String, Object>(), r, null);
        assertTrue(executed);
    }
    
    public void testDefaultActionExistingMethod() throws Exception {
        Executor executor = new Executor(defaultMatchers(), new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/toString";
        
        executor.exec(new Page2(), new HashMap<String, Object>(), r, null);
        assertFalse(executed);
    }
    
    public class Page3 extends Page2 {
        public boolean executed;
        
        public Outcome execute(String haha) {
            this.executed = true;
            
            return new NoOutcome();
        }
        
        @DefaultAction
        public Outcome executezzz(String haha) {
            this.executed = true;
            
            return new NoOutcome();
        }
    }
    
    public void testSameMethodInBaseSuperClass() throws Exception {
        Executor executor = new Executor(defaultMatchers(), new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/execute";
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), r, null);
        
        assertFalse(executed);
        assertTrue(page.executed);
    }
    
    public void testDefaultActionInBaseSuperClass() throws Exception {
        Executor executor = new Executor(defaultMatchers(), new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/execute2";
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), r, null);
        
        assertFalse(executed);
        assertTrue(page.executed);
    }
}
