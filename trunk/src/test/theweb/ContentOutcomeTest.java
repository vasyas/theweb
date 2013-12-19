package theweb;

import junit.framework.TestCase;
import theweb.i18n.Resources;

public class ContentOutcomeTest extends TestCase {
    public void testContentOutcomeWillNotClearMessages() throws Exception {
        Resources.setLocale("en");

        ContentOutcome contentOutcome = new ContentOutcome("abc");
        
        new Messages().set();
        Messages.error("123");
        
        contentOutcome.process(new AbstractPage("/"), new MockHttpExchange("/"));
        
        assertTrue(Messages.hasErrors());
    }
}
