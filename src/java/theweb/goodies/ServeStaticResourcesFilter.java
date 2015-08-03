package theweb.goodies;

import com.sun.net.httpserver.HttpExchange;
import theweb.resources.ClasspathResourceLocation;
import theweb.resources.ResourceLocation;

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

    private String getContentType(String path) {
        Map<String, String> types = new HashMap<>();
        
        types.put(".css", "text/css");
        types.put(".scss", "text/plain");
        types.put(".gif", "image/gif");
        types.put(".png", "image/png");
        types.put(".jpg", "image/jpeg");
        types.put(".mp3", "audio/mpeg");
        types.put(".wav", "audio/wav");
        types.put(".js", "application/javascript");
        types.put(".jsp", "application/javascript");
        types.put(".jar", "application/java-archive");
        types.put(".woff", "application/font-woff");
        types.put(".svg", "image/svg+xml");
        types.put(".html", "text/html");
        types.put(".map", "application/octet-stream");

        if (!path.contains(".")) return null;
        
        path = path.substring(path.lastIndexOf('.'));
        
        return types.get(path);
    }
    
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String contentType = getContentType(exchange.getRequestURI().getPath());

        if (contentType == null) {
            chain.doFilter(exchange);
            return;
        }

        ResourceLocation resourceLoc = getResourceLocation(exchange);
        InputStream inputStream = null;

        if (resourceLoc != null) inputStream = resourceLoc.getInputStream();

        if (inputStream == null) {
            String msg = "404 - Not found";
            
            exchange.sendResponseHeaders(404, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.getResponseBody().close();
            exchange.close();
            return;
        }

        inputStream = resourceLoc.getInputStream();

        // check for etag
        String etag = exchange.getRequestHeaders().getFirst("If-None-Match");

        if (etag != null && etag.equals("" + resourceLoc.lastModified())) {
            exchange.getResponseHeaders().add("Cache-Control", "max-age=120");
            exchange.getResponseHeaders().add("ETag", "" + resourceLoc.lastModified());
            exchange.sendResponseHeaders(304, -1);
            exchange.close();
            return;
        }

        // send response
        try {
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.getResponseHeaders().add("Cache-Control", "max-age=120");
            exchange.getResponseHeaders().add("ETag", "" + resourceLoc.lastModified());
            exchange.getResponseHeaders().add("Cache-Control", "no-cache");


            exchange.sendResponseHeaders(200, 0);
            
            BufferedOutputStream responseBody = new BufferedOutputStream(exchange.getResponseBody());
            
            BufferedInputStream resource = new BufferedInputStream(inputStream);
            
            int i;
            
            while ((i = resource.read()) != -1)
                responseBody.write(i);
            
            resource.close();
            responseBody.close();
        } finally {
            exchange.close();
        }
    }

    protected ResourceLocation getResourceLocation(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();

        path = path.substring(exchange.getHttpContext().getPath().length());

        if (path.startsWith("/")) path = path.substring(1);

        return new ClasspathResourceLocation(path);
    }

}