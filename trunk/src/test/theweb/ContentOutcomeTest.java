package theweb;

import junit.framework.TestCase;

public class ContentOutcomeTest extends TestCase {
    public void testContentOutcomeWillNotClearMessages() throws Exception {
        ContentOutcome contentOutcome = new ContentOutcome("abc");
        
        new Messages().set();
        Messages.error("123");
        
        contentOutcome.process(new AbstractPage("/"), new MockHttpExchange("/"));
        
        assertTrue(Messages.hasErrors());
    }
}
