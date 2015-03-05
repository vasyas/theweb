package theweb;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.Map;

public class MockHttpExchange implements HttpExchange {
    private final String requestPath;

    public MockHttpExchange(String requestPath) {
        this.requestPath = requestPath;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getRequestPath() {
        return requestPath;
    }
    
    @Override
    public String getRequestMethod() {
        return "";
    }
    
    @Override
    public String getRequestQuery() {
        return "";
    }

    @Override
    public String getRequestHeader(String name) {
        return null;
    }

    @Override
    public Map<String, Object> getRequestParameters() {
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
    
    @Override
    public void sendError(int error, String msg) {
    }

    @Override
    public void sendRedirect(String to) {
    }

    @Override
    public void addResponseHeader(String name, String value) {
    }

    @Override
    public InetSocketAddress getRemoteAddr() {
        return null;
    }
    
}