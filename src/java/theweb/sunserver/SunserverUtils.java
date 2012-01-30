package theweb.sunserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

class SunserverUtils {
    private final static Logger log = Logger.getLogger(SunserverUtils.class);
    
    public static Map<String, Object> readParameters(HttpExchange exchange) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        try {
            parameters.putAll(parseQuery(exchange.getRequestURI().getRawQuery()));
            
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                
                br.close();
                
                parameters.putAll(parseQuery(query));
            }
        } catch(IOException e) {
            log.warn("Can't parse request", e);
        }
        
        return parameters;
    }
    
    private static Map<String, Object> parseQuery(String query) throws UnsupportedEncodingException {
        Map<String, Object> r = new HashMap<String, Object>();

        if (query == null) return r;
        
        String pairs[] = query.split("[&]");

        for (String pair : pairs) {
            String param[] = pair.split("[=]");

            if (param.length == 0) continue;
            
            String key = URLDecoder.decode(param[0], "UTF-8");
            Object value = null;
            
            if (param.length > 1)
                value = URLDecoder.decode(param[1], "UTF-8");

            if (r.containsKey(key)) {
                Object old = r.get(key);
                
                if (old instanceof String) {
                    value = new String[] { (String) old, (String) value };
                } else { 
                    old = Arrays.copyOf((String[]) old, Array.getLength(old) + 1);
                    Array.set(old, Array.getLength(old) + 1, value);
                    
                    value = old;
                } 
            }
            
            r.put(key, value);
        }
        
        return r;
    }
}
