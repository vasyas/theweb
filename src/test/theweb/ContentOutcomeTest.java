package theweb;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import junit.framework.TestCase;

public class ContentOutcomeTest extends TestCase {
    public void testContentOutcomeWillNotClearMessages() throws Exception {
        ContentOutcome contentOutcome = new ContentOutcome("abc");
        
        new Messages().set();
        Messages.error("123");
        
        contentOutcome.process(new AbstractPage("/"), new MockHttpExchange());
        
        assertTrue(Messages.hasErrors());
    }
    
    public class MockHttpExchange implements HttpExchange {
        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getRequestPath() {
            return null;
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
            return new PrintWriter(new StringWriter());
        }

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public void sendError(int error) {
        }

        public String redirectedTo;
        
        @Override
        public void sendRedirect(String url) {
            this.redirectedTo = url;
        }

        @Override
        public void addHeader(String name, String value) {
        }
        
    }
}
