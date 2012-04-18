package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import theweb.ContentOutcome;
import theweb.HttpExchange;
import theweb.NoOutcome;
import theweb.Outcome;
import theweb.Page;
import theweb.test.TestPage;

public class PageExecutionTest extends TestCase {
    public void testInvokationCompleted() throws Exception {
        List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
        StringBuilder sequence = new StringBuilder();

        interceptors.add(new DelegatingInterceptor("1", sequence));
        interceptors.add(new DelegatingInterceptor("2", sequence));
        interceptors.add(new DelegatingInterceptor("3", sequence));

        Object outcome = new ChainedActionExecution(interceptors, null, mockExecution).execute();
        assertEquals(mockOutcome, outcome);
        assertEquals("123", sequence.toString());
    }
    
    public void testInvokationTerminated() throws Exception {
        List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
        StringBuilder sequence = new StringBuilder();

        ContentOutcome o1 = new ContentOutcome("-1", "");
        
        interceptors.add(new DelegatingInterceptor("1", sequence));
        interceptors.add(new TerminatingInterceptor("t", o1, sequence));
        interceptors.add(new DelegatingInterceptor("3", sequence));

        Object outcome = new ChainedActionExecution(interceptors, null, mockExecution).execute();
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
            new ChainedActionExecution(interceptors, null, mockExecution).execute();
            fail();
        } catch (RuntimeException e) {
            assertEquals("1e", sequence.toString());
        }
    }

    NoOutcome mockOutcome = new NoOutcome();
    Execution mockExecution = new Execution() {
        @Override
        public Outcome execute() {
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
        public Object execute(Execution execution, HttpExchange exchange) throws IOException {
            sequence.append(name);
            return execution.execute();
        }
    }

    private class TerminatingInterceptor implements PageInterceptor {
        private String name;
        private Outcome value;
        private StringBuilder sequence;

        public TerminatingInterceptor(String name, Outcome value, StringBuilder invoked) {
            this.value = value;
            this.name = name;
            this.sequence = invoked;
        }

        @Override
        public Outcome execute(Execution execution, HttpExchange exchange) {
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
        public Outcome execute(Execution execution, HttpExchange exchange) {
            sequence.append(name);
            throw new RuntimeException("not found");
        }
    }
    
    public void testInaccessibleMethod() throws Exception {
        MethodExecution execution = new MethodExecution();

        execution.page = TestPage.page;
        execution.args = new Object[0];
        execution.m = TestPage.page.getClass().getDeclaredMethod("action1");
        
        execution.execute();
        
        execution.m = TestPage.page.getClass().getDeclaredMethod("action2");
        
        try {
            execution.execute();
            fail();
        } catch(RuntimeException e) {
        }
    }
}
