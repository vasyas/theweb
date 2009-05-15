package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import theweb.ContentOutcome;
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

        Outcome outcome = new ChainedActionExecution(interceptors, null, null, mockExecution).execute();
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

        Outcome outcome = new ChainedActionExecution(interceptors, null, null, mockExecution).execute();
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
            new ChainedActionExecution(interceptors, null, null, mockExecution).execute();
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
    };

    private class DelegatingInterceptor implements PageInterceptor {
        private String name;
        private StringBuilder sequence;

        public DelegatingInterceptor(String name, StringBuilder invoked) {
            this.name = name;
            this.sequence = invoked;
        }

        @Override
        public Outcome execute(Execution execution, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        public Outcome execute(Execution execution, HttpServletRequest request, HttpServletResponse response) {
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
        public Outcome execute(Execution execution, HttpServletRequest request, HttpServletResponse response) {
            sequence.append(name);
            throw new RuntimeException("not found");
        }
    }
    
    public void testInaccessibleMethod() throws Exception {
        PageExecution execution = new PageExecution();

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
