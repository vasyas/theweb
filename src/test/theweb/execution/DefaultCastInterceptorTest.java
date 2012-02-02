package theweb.execution;

import junit.framework.TestCase;
import theweb.AbstractPage;
import theweb.ContentOutcome;
import theweb.Markup;
import theweb.html.StringMarkup;

public class DefaultCastInterceptorTest extends TestCase {
    @SuppressWarnings("unused")
    public void testConvertMarkupToContentOutcome() throws Exception {
        MethodExecution execution = new MethodExecution();
        
        execution.page = new AbstractPage("/") {
            public Markup exec() {
                return new StringMarkup("abc");
            }
        };
        
        execution.m = execution.page.getClass().getDeclaredMethod("exec");
        execution.args = new Object[0];
        
        Object execute = new DefaultCastInterceptor().execute(execution, null, null);
        
        assertTrue(execute instanceof ContentOutcome);
    }

}
