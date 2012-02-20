package theweb;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import theweb.execution.Executor;
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
    
    private class MockHttpExchange implements HttpExchange {
        private final String requestPath;

        public MockHttpExchange(String requestPath) {
            this.requestPath = requestPath;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getRequestPath() {
            return requestPath;
        }

        @Override
        public String getHeader(String name) {
            return null;
        }

        @Override
        public Map<String, Object> getParameters() {
            return null;
        }
        
        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public void setContentType(String contentType) {
        }

        @Override
        public void setContentLength(long size) {
        }

        @Override
        public PrintWriter getWriter() {
            return null;
        }

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public void sendError(int error) {
        }

        @Override
        public void sendRedirect(String to) {
        }

        @Override
        public void addHeader(String name, String value) {
        }
        
    }
    
    public void testDefaultAction() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/"));
        assertTrue(executed);
    }
    
    public void testDefaultActionNoMatchingMethod() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/asdfasdf"));
        assertTrue(executed);
    }
    
    public void testDefaultActionExistingMethod() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        executor.exec(new Page2(), new HashMap<String, Object>(), new MockHttpExchange("/toString"));
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
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), new MockHttpExchange("/execute"));
        
        assertFalse(executed);
        assertTrue(page.executed);
    }
    
    public void testDefaultActionInBaseSuperClass() throws Exception {
        Executor executor = new Executor(new ArrayList<PageInterceptor>());
        
        Page3 page = new Page3();
        executor.exec(page, new HashMap<String, Object>(), new MockHttpExchange("/execute2"));
        
        assertFalse(executed);
        assertTrue(page.executed);
    }
}
