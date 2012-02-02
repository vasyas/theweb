package theweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

public class ContentOutcomeTest extends TestCase {
    public void testContentOutcomeWillNotClearMessages() throws Exception {
        ContentOutcome contentOutcome = new ContentOutcome("abc");
        
        new Messages().set();
        Messages.error("123");
        
        contentOutcome.process(new AbstractPage("/"), null, new MockHttpServletResponse());
        
        assertTrue(Messages.hasErrors());
    }
    
    public class MockHttpServletResponse implements HttpServletResponse {

        @Override
        public void addCookie(Cookie arg0) {
        }

        @Override
        public void addDateHeader(String arg0, long arg1) {
        }

        @Override
        public void addHeader(String arg0, String arg1) {
        }

        @Override
        public void addIntHeader(String arg0, int arg1) {
        }

        @Override
        public boolean containsHeader(String arg0) {
            return false;
        }

        @Override
        public String encodeRedirectURL(String arg0) {
            return null;
        }

        @Override
        public String encodeRedirectUrl(String arg0) {
            return null;
        }

        @Override
        public String encodeURL(String arg0) {
            return null;
        }

        @Override
        public String encodeUrl(String arg0) {
            return null;
        }

        @Override
        public void sendError(int arg0) throws IOException {
        }

        @Override
        public void sendError(int arg0, String arg1) throws IOException {
        }

        public String redirectedTo;
        
        @Override
        public void sendRedirect(String url) throws IOException {
            this.redirectedTo = url;
        }

        @Override
        public void setDateHeader(String arg0, long arg1) {
        }

        @Override
        public void setHeader(String arg0, String arg1) {
        }

        @Override
        public void setIntHeader(String arg0, int arg1) {
        }

        @Override
        public void setStatus(int arg0) {
        }

        @Override
        public void setStatus(int arg0, String arg1) {
        }

        @Override
        public void flushBuffer() throws IOException {
        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new StringWriter());
        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {
        }

        @Override
        public void resetBuffer() {
        }

        @Override
        public void setBufferSize(int arg0) {
        }

        @Override
        public void setCharacterEncoding(String arg0) {
        }

        @Override
        public void setContentLength(int arg0) {
        }

        @Override
        public void setContentType(String arg0) {
        }

        @Override
        public void setLocale(Locale arg0) {
        }
    }
}
