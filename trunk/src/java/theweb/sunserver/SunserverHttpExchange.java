package theweb.sunserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
        return exchange.getRequestURI().getPath();
    }

    @Override
    public String getHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }
    
    private Map<String, Object> parameters;

    @Override
    public Map<String, Object> getParameters() {
        if (parameters == null) 
            parameters = SunserverUtils.readParameters(exchange);
        
        return parameters;
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
        addHeader("Content-Type", contentType);
    }

    @Override
    public void setContentLength(long length) {
        this.contentLength = length;
        
        addHeader("Content-Length", "" + length);
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
            
            addHeader("Location", to);
            exchange.sendResponseHeaders(302, -1);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        exchange.getResponseHeaders().add(name, value);
    }
}
