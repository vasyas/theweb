package theweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used to transform page state into a set of request parameters
 * 
 * Can be used to create page links with initial state
 * Thread-local instance is used to create links with current page's state
 */
public class PageState {
    private final Map<String, String[]> parameterMap;
    private final PathPattern pathPattern;

    public PageState(Page page) {
        this(page.getPathPattern());
        
        new Describer(parameterMap).describe(page);
    }
    
    private PageState(PathPattern pathPattern) {
        this.pathPattern = pathPattern;
        parameterMap = new HashMap<String, String[]>();
    }
    
    public static PageState getCurrent() {
        return tl.get();
    }

    static void clear() {
        tl.set(null);
    }
    
    static void setCurrent(PageState current) {
        tl.set(current);
    }
    
    private final static ThreadLocal<PageState> tl = new ThreadLocal<PageState>();

    public String view() {
        return action("");
    }
    
    /** Convenient method for velocity */
    public String action(String method) {
        return action(method, new String[] { });
    }
    
    /** Convenient method for velocity */
    public String action(String method, String param1, String param2) {
        if (param1 == null || param2 == null) return action(method);
        
        return action(method, new String[] { param1, param2 });
    }
    
    /** Add page state to action paramters. Will return local link */
    public String action(String method, String ... params) {
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>(parameterMap);
        
        for (int i = 0; i < params.length; i = i + 2)
            addParameter(parameters, (String) params[i], params[i + 1]);
        
        return link(parameters, method);
    }

    /** Return page state as form inputs */
    public String form() {
        StringBuilder url = new StringBuilder();
        
        for (String key : parameterMap.keySet()) {
            String[] array = parameterMap.get(key);
            
            for (int i = 0; i < array.length; i ++) {
                String value = array[i];
                
                url.append("<input type=\"hidden\" name=\"");
                url.append(key);
                url.append("\" value=\"");
                url.append(value.toString());
                url.append("\">");
            }
        }
        
        return url.toString();
    }
    
    private String link(Map<String, String[]> parameters, String method) {
        StringBuilder url = new StringBuilder();
        
        url.append(ContextInfo.getCurrent().contextPath);
        url.append(pathPattern.createPath(parameters));
        
        url.append(method);
        
        for (String key : parameters.keySet()) {
            String[] array = parameters.get(key);
            
            for (int i = 0; i < array.length; i ++) {
                String value = array[i];
                
                if (url.indexOf("?") != -1)
                    url.append("&");
                else
                    url.append("?");
                
                url.append(key);
                url.append("=");
                
                try {
                    url.append(URLEncoder.encode(value.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        
        return url.toString();
    }

    private void addParameter(Map<String, String[]> parameters, String name, String value) {
        if (!parameters.containsKey(name)) {
            String[] array = new String[1];
            array[0] = value;
            parameters.put(name, array);
        } else {
            String[] array = parameters.get(name);
            String[] newarray = new String[array.length + 1];
            System.arraycopy(array, 0, newarray, 0, array.length);
            newarray[array.length] = value;
            parameters.put(name, newarray);
        }
    }

    public PageState clean() {
        return new PageState(pathPattern);
    }
}