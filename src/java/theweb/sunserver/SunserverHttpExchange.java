package theweb.sunserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import theweb.HttpExchange;

public class SunserverHttpExchange implements HttpExchange {
    private final com.sun.net.httpserver.HttpExchange exchange;

    public SunserverHttpExchange(com.sun.net.httpserver.HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getContextPath() {
        String path = exchange.getHttpContext().getPath();
        
        if (path.startsWith("/"))
            path = path.substring(1);
        
        return path;
    }

    @Override
    public String getRequestPath() {
        String path = exchange.getRequestURI().getPath();

        String context = exchange.getHttpContext().getPath();

        path = "/" + path.substring(context.length());

        return path;
    }
    
    @Override
    public String getRequestQuery() {
        return exchange.getRequestURI().getQuery();
    }
    
    @Override
    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }

    @Override
    public String getRequestHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }
    
    private Map<String, Object> parameters;

    @Override
    public Map<String, Object> getRequestParameters() {
        if (parameters == null) 
            parameters = SunserverUtils.readParameters(exchange);
        
        return parameters;
    }
    
    @Override
    public InputStream getInputStream() {
        return exchange.getRequestBody();
    }
    
    @Override
    public InetSocketAddress getRemoteAddr() {
        return exchange.getRemoteAddress();
    }
    
    //
    // Response
    //
    
    private boolean contentHeadersSent;
    private Long contentLength;
    
    private void sendContentHeaders() {
        contentHeadersSent = true;

        try {
            exchange.sendResponseHeaders(200, contentLength == null ? 0 : contentLength);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setContentType(String contentType) {
        addResponseHeader("Content-Type", contentType);
    }

    @Override
    public void setContentLength(long length) {
        this.contentLength = length;
        
        addResponseHeader("Content-Length", "" + length);
    }

    @Override
    public PrintWriter getWriter() {
        if (!contentHeadersSent) sendContentHeaders();
        
        return new PrintWriter(exchange.getResponseBody());
    }

    @Override
    public OutputStream getOutputStream() {
        if (!contentHeadersSent) sendContentHeaders();
        
        return exchange.getResponseBody();
    }

    private final static Map<Integer, String> errors = new HashMap<Integer, String>() {{ 
        put(404, "404 - Not found");
    }};
    
    @Override
    public void sendError(int code) {
        String content = errors.get(code);
        
        if (content == null) content = "" + code;
        
        sendError(code, content);
    }
    
    @Override
    public void sendError(int code, String content) {
        try {
            contentHeadersSent = true;
            
            exchange.sendResponseHeaders(code, content.length());
            
            PrintWriter w = getWriter();
            
            w.write(content);
            
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendRedirect(String to) {
        try {
            contentHeadersSent = true;
            
            addResponseHeader("Location", to);
            exchange.sendResponseHeaders(302, -1);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addResponseHeader(String name, String value) {
        exchange.getResponseHeaders().add(name, value);
    }
}
