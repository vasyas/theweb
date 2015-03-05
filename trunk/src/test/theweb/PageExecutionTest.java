package theweb;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageExecutionTest extends TestCase {
    public void testInvokationCompleted() throws Exception {
        List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
        StringBuilder sequence = new StringBuilder();

        interceptors.add(new DelegatingInterceptor("1", sequence));
        interceptors.add(new DelegatingInterceptor("2", sequence));
        interceptors.add(new DelegatingInterceptor("3", sequence));

        Object outcome = new InterceptedExecution(interceptors, null, mockExecution).execute();
        assertEquals(mockOutcome, outcome);
        assertEquals("123", sequence.toString());
    }
    
    public void testInvokationTerminated() throws Exception {
        List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
        StringBuilder sequence = new StringBuilder();

        ContentResponse o1 = new ContentResponse("-1", "");
        
        interceptors.add(new DelegatingInterceptor("1", sequence));
        interceptors.add(new TerminatingInterceptor("t", o1, sequence));
        interceptors.add(new DelegatingInterceptor("3", sequence));

        Object outcome = new InterceptedExecution(interceptors, null, mockExecution).execute();
        assertEquals(o1, outcome);
        assertEquals("1t", sequence.toString());
    }

    public void testInvokationBroken() throws Exception {
        List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
        StringBuilder sequence = new StringBuilder();

        interceptors.add(new DelegatingInterceptor("1", sequence));
        interceptors.add(new ExceptionalInterceptor("e", sequence));
        interceptors.add(new DelegatingInterceptor("3", sequence));

        try {
            new InterceptedExecution(interceptors, null, mockExecution).execute();
            fail();
        } catch (RuntimeException e) {
            assertEquals("1e", sequence.toString());
        }
    }

    NoResponse mockOutcome = new NoResponse();
    Execution mockExecution = new Execution() {
        @Override
        public Response execute() {
            return mockOutcome;
        }

        @Override
        public Method getMethod() {
            return null;
        }

        @Override
        public Page getPage() {
            return null;
        }
        
        public Object[] getArgs() {
            return null;
        }
    };

    private class DelegatingInterceptor implements PageInterceptor {
        private String name;
        private StringBuilder sequence;

        public DelegatingInterceptor(String name, StringBuilder invoked) {
            this.name = name;
            this.sequence = invoked;
        }

        @Override
        public Object execute(Execution execution, HttpExchange exchange) throws Exception {
            sequence.append(name);
            return execution.execute();
        }
    }

    private class TerminatingInterceptor implements PageInterceptor {
        private String name;
        private Response value;
        private StringBuilder sequence;

        public TerminatingInterceptor(String name, Response value, StringBuilder invoked) {
            this.value = value;
            this.name = name;
            this.sequence = invoked;
        }

        @Override
        public Response execute(Execution execution, HttpExchange exchange) {
            sequence.append(name);
            return value;
        }
    }

    private class ExceptionalInterceptor implements PageInterceptor {
        private String name;
        private StringBuilder sequence;

        public ExceptionalInterceptor(String name, StringBuilder invoked) {
            this.name = name;
            this.sequence = invoked;
        }

        @Override
        public Response execute(Execution execution, HttpExchange exchange) {
            sequence.append(name);
            throw new RuntimeException("not found");
        }
    }
    
    public void testInaccessibleMethod() throws Exception {
        MethodExecution execution = new MethodExecution(TestPage.page, TestPage.page.getClass().getDeclaredMethod("action1"), new HashMap<>());

        execution.execute();
        
        execution.method = TestPage.page.getClass().getDeclaredMethod("action2");
        
        try {
            execution.execute();
            fail();
        } catch(RuntimeException e) {
        }
    }
}
