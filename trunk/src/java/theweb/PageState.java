package theweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        this.pathPattern = new PathPattern(page.path);
        parameterMap = new LinkedHashMap<>();
        
        new Describer(parameterMap).describe(page);
    }
    
    public static PageState get() {
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
    
    /** Add page state to action paramters. Will return local link */
    public String action(String method, String... params) {
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>(parameterMap);
        
        for (int i = 0; i < params.length; i = i + 2)
            add(parameters, (String) params[i], params[i + 1]);
        
        return link(parameters, method);
    }

    private String link(Map<String, String[]> parameters, String method) {
        StringBuilder url = new StringBuilder();
        
        url.append(pathPattern.createPath(parameters));
        
        url.append(method);
        
        for (String key : parameters.keySet()) {
            String[] array = parameters.get(key);

            for (String value : array) {
                if (url.indexOf("?") != -1)
                    url.append("&");
                else
                    url.append("?");

                url.append(key);
                url.append("=");

                try {
                    url.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException ignored) {
                }
            }
        }
        
        return url.toString();
    }

    private static void add(Map<String, String[]> parameters, String name, String value) {
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

    public static String view(Page page) {
        return new PageState(page).view();
    }

    public static String action(Page page, String method, String... params) {
        return new PageState(page).action(method, params);
    }
}