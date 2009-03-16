package theweb;

import java.util.HashMap;
import java.util.Map;

public class PathPattern {
    private final String pattern;

    public PathPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public Map<String, String> parse(String path) {
        String templateTokens[] = pattern.split("/");
        String servletTokens[] = path.split("/");
        
        Map<String, String> map = new HashMap<String, String>();
        
        for (int i = 0; i < templateTokens.length; i ++) {
            String varName = getVarName(templateTokens[i]);
            
            if (varName != null && i < servletTokens.length)
                map.put(varName, servletTokens[i]);
        }
        
        return map;
    }
    
    private String getVarName(String t) {
        if (t.isEmpty()) return null;
        if (t.charAt(0) != '{' || t.charAt(t.length() - 1) != '}') return null;
        
        return t.substring(0 + 1, t.length() - 1);
    }
}
