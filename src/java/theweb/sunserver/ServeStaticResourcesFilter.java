package theweb.sunserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServeStaticResourcesFilter extends com.sun.net.httpserver.Filter {
    @Override
    public String description() {
        return "ServeStaticResourcesFilters";
    }

    private boolean eligible(HttpExchange exchange) {
        return getContentType(exchange.getRequestURI().getPath()) != null;
    }
    
    private String getContentType(String path) {
        Map<String, String> types = new HashMap<String, String>();
        
        types.put(".css", "text/css");
        types.put(".html", "text/html");
        types.put(".gif", "image/gif");
        types.put(".png", "image/png");
        types.put(".jpg", "image/jpeg");
        types.put(".mp3", "audio/mpeg");
        types.put(".wav", "audio/wav");
        types.put(".js", "application/javascript");
        types.put(".jsp", "application/javascript");
        types.put(".jar", "application/java-archive");

        if (!path.contains(".")) return null;
        
        path = path.substring(path.lastIndexOf('.'));
        
        return types.get(path);
    }
    
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        if (!eligible(exchange)) {
            chain.doFilter(exchange);
            return;
        }
        
        String path = exchange.getRequestURI().getPath();

        path = path.substring(exchange.getHttpContext().getPath().length());

        if (path.startsWith("/")) path = path.substring(1);
        
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        
        if (is == null) {
            String msg = "404 - Not found";
            
            exchange.sendResponseHeaders(404, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.getResponseBody().close();
            exchange.close();
            return;
        }
        
        try {
            String contentType = getContentType(path);
            
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, 0);
            
            BufferedOutputStream responseBody = new BufferedOutputStream(exchange.getResponseBody());
            
            BufferedInputStream resource = new BufferedInputStream(is);
            
            int i;
            
            while ((i = is.read()) != -1) 
                responseBody.write(i);
            
            resource.close();
            responseBody.close();
        } finally {
            exchange.close();
        }
    }
    
}
