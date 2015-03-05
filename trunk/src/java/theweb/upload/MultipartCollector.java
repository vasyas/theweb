package theweb.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import theweb.Collector;
import theweb.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartCollector implements Collector {
    private class ContentType {
        private String contentType;
        private Map<String, String> parameters = new HashMap<String, String>();
        
        public ContentType(String def) {
            if (def == null) return;
                
            String[] parts = def.split(";");
            
            this.contentType = def;
            
            for (int i = 1; i < parts.length; i ++) {
                String[] s = parts[i].split("=");
                
                parameters.put(s[0].trim(), s[1].trim());
            }
        }
        
        public String getContentType() {
            return contentType;
        }
        
        public String getParameter(String name) {
            return parameters.get(name);
        }
    }
    
    private class SunServerRequestContext implements RequestContext {
        private final HttpExchange exchange;

        public SunServerRequestContext(HttpExchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public String getCharacterEncoding() {
            return contentType().getParameter("encoding");
        }

        @Override
        public int getContentLength() {
            String first = exchange.getRequestHeader("Content-Length");
            
            if (first == null) return 0;
            
            return Integer.parseInt(first);
        }

        @Override
        public String getContentType() {
            return contentType().getContentType();
        }
        
        private ContentType contentType() {
            String s = exchange.getRequestHeader("Content-Type");

            return new ContentType(s);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return exchange.getInputStream();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void collect(Map<String, Object> properties, HttpExchange exchange) {
        SunServerRequestContext requestContext = new SunServerRequestContext(exchange);
        
        if (ServletFileUpload.isMultipartContent(requestContext)) {
            FileItemFactory factory = new DiskFileItemFactory(); 
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            try {
                for (FileItem item : (List<FileItem>) upload.parseRequest(requestContext)) {
                    if (item.isFormField()) {
                        properties.put(item.getFieldName(), item.getString());
                    } else {
                        if (item.getName().isEmpty()) continue; // not uploaded
                        
                        Upload u = new Upload();
                        
                        u.contentType = item.getContentType();
                        u.name = item.getName();
                        u.size = (int) item.getSize();
                        u.setContent(item.getInputStream());
                        
                        u.name = u.name.replace('\\', '/');
                        
                        if (u.name.contains("/"))
                        	u.name = u.name.substring(u.name.lastIndexOf("/") + 1);

                        Object r;

                        if (properties.containsKey(item.getFieldName())) {
                            Object old = properties.get(item.getFieldName());

                            if (old.getClass().isArray()) {
                                int newLength = ((Upload[]) old).length + 1;
                                r = Arrays.copyOf((Upload[]) old, newLength);
                                Array.set(r, newLength - 1, u);
                            } else
                                r = new Upload[] {(Upload) old, u};
                        } else
                            r = u;

                        properties.put(item.getFieldName(), r);
                    }
                }
            } catch(FileUploadException | IOException e) {
                throw new RuntimeException(e);
			}
        }
    }
}
