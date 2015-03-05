package theweb.goodies;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class SunserverHttpExchangeTest extends TestCase {
    public void testRequestPathDoesntIncludeContextPath() {
        HttpExchange httpExchange = httpExchange("/context/", "/context/servlet/path");

        SunserverHttpExchange e = new SunserverHttpExchange(httpExchange);

        assertEquals("/servlet/path", e.getRequestPath());
    }

    public void testEmpty() {
        HttpExchange httpExchange = httpExchange("/", "/servlet/path");

        SunserverHttpExchange e = new SunserverHttpExchange(httpExchange);

        assertEquals("/servlet/path", e.getRequestPath());
    }

    public void testContextPathEmpty() {
        HttpExchange httpExchange = httpExchange("/", "/servlet/path");

        SunserverHttpExchange e = new SunserverHttpExchange(httpExchange);

        assertEquals("", e.getContextPath());
    }

    public void testContextPathTrailing() {
        HttpExchange httpExchange = httpExchange("/context/", "/servlet/path");

        SunserverHttpExchange e = new SunserverHttpExchange(httpExchange);

        assertEquals("/context", e.getContextPath());
    }

    private HttpExchange httpExchange(final String context, final String requestPath) {
        return new HttpExchange() {
                @Override
                public Headers getRequestHeaders() {
                    return null;
                }

                @Override
                public Headers getResponseHeaders() {
                    return null;
                }

                @Override
                public URI getRequestURI() {
                    try {
                        return new URI(requestPath);
                    } catch (URISyntaxException e1) {
                        throw new RuntimeException(e1);
                    }
                }

                @Override
                public String getRequestMethod() {
                    return null;
                }

                @Override
                public HttpContext getHttpContext() {
                    return new HttpContext() {
                        @Override
                        public HttpHandler getHandler() {
                            return null;
                        }

                        @Override
                        public void setHandler(HttpHandler httpHandler) {

                        }

                        @Override
                        public String getPath() {
                            return context;
                        }

                        @Override
                        public HttpServer getServer() {
                            return null;
                        }

                        @Override
                        public Map<String, Object> getAttributes() {
                            return null;
                        }

                        @Override
                        public List<Filter> getFilters() {
                            return null;
                        }

                        @Override
                        public Authenticator setAuthenticator(Authenticator authenticator) {
                            return null;
                        }

                        @Override
                        public Authenticator getAuthenticator() {
                            return null;
                        }
                    };
                }

                @Override
                public void close() {

                }

                @Override
                public InputStream getRequestBody() {
                    return null;
                }

                @Override
                public OutputStream getResponseBody() {
                    return null;
                }

                @Override
                public void sendResponseHeaders(int i, long l) throws IOException {

                }

                @Override
                public InetSocketAddress getRemoteAddress() {
                    return null;
                }

                @Override
                public int getResponseCode() {
                    return 0;
                }

                @Override
                public InetSocketAddress getLocalAddress() {
                    return null;
                }

                @Override
                public String getProtocol() {
                    return null;
                }

                @Override
                public Object getAttribute(String s) {
                    return null;
                }

                @Override
                public void setAttribute(String s, Object o) {

                }

                @Override
                public void setStreams(InputStream inputStream, OutputStream outputStream) {

                }

                @Override
                public HttpPrincipal getPrincipal() {
                    return null;
                }
            };
    }
}
