package theweb.goodies;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class SunserverUtilsTest extends TestCase {
    public void testEmptyParams() throws Exception {
        HttpExchange exchange = new HttpExchange() {
            
            @Override
            public void setStreams(InputStream arg0, OutputStream arg1) {
            }
            
            @Override
            public void setAttribute(String arg0, Object arg1) {
            }
            
            @Override
            public void sendResponseHeaders(int arg0, long arg1) throws IOException {
            }
            
            @Override
            public Headers getResponseHeaders() {
                return null;
            }
            
            @Override
            public int getResponseCode() {
                return 0;
            }
            
            @Override
            public OutputStream getResponseBody() {
                return null;
            }
            
            @Override
            public URI getRequestURI() {
                try {
                    return new URI("http://server.com/?key1=value1&key2=");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            
            @Override
            public String getRequestMethod() {
                return "GET";
            }
            
            @Override
            public Headers getRequestHeaders() {
                return null;
            }
            
            @Override
            public InputStream getRequestBody() {
                return null;
            }
            
            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }
            
            @Override
            public String getProtocol() {
                return null;
            }
            
            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
            
            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }
            
            @Override
            public HttpContext getHttpContext() {
                return null;
            }
            
            @Override
            public Object getAttribute(String arg0) {
                return null;
            }
            
            @Override
            public void close() {
            }
        };
        
        Map<String, Object> m = SunserverUtils.readParameters(exchange);
        
        assertEquals("value1", m.get("key1"));
        assertEquals("", m.get("key2"));
    }
}
