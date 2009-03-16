package theweb;

import java.util.ArrayList;

import junit.framework.TestCase;
import theweb.execution.Executor;
import theweb.execution.PageInterceptor;

public class ExecutorTest extends TestCase {
    
    boolean executed;
    
    public class Page2 extends Page {
        public Page2() {
            super("/");
        }

        @DefaultAction
        public Outcome execute() {
            executed = true;
            
            return new RenderOutcome();
        }
    }
    
    public void testDefaultAction() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/";
        
        executor.exec(new Page2(), r, null);
        assertTrue(executed);
    }
    
    public void testDefaultActionNoMatchingMethod() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/asdfasdf";
        
        executor.exec(new Page2(), r, null);
        assertTrue(executed);
    }
    
    public void testDefaultActionExistingMethod() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        MockHttpServletRequest r = new MockHttpServletRequest();
        r.servletPath = "/toString";
        
        executor.exec(new Page2(), r, null);
        assertFalse(executed);
    }
}
