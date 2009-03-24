package theweb;

import java.util.HashMap;
import java.util.Map;

public class PathPattern {
    public final String pattern;

    public PathPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public Map<String, String> matches(String path) {
        String templateTokens[] = pattern.split("/");
        String servletTokens[] = path.split("/");
        
        Map<String, String> map = new HashMap<String, String>();
        
        for (int i = 0; i < templateTokens.length; i ++) {
            String varName = getVarName(templateTokens[i]);
            
            if (varName == null) // not variable - should be equal
                if (!servletTokens[i].equals(templateTokens[i])) 
                    return null;
            
            if (varName != null && i < servletTokens.length)
                map.put(varName, servletTokens[i]);
        }
        
        return map;
    }
    
    public String createPath(Map<String, String[]> properties) {
        String templateTokens[] = pattern.split("/", -1);
        
        StringBuilder s = new StringBuilder();
        
        for (int i = 0; i < templateTokens.length; i ++) {
            String varName = getVarName(templateTokens[i]);
            
            
            if (varName == null)
                s.append(templateTokens[i]);
            else {
                if (!properties.containsKey(varName)) throw new RuntimeException("Properties do not contain key " + varName);
                
                String[] prop = properties.get(varName);
                properties.remove(varName);
                
                if (prop == null) return s.toString();
                
                if (prop.length > 1) throw new RuntimeException("Only single valued properties allowed in path, but " + varName + "=" + prop);
                
                s.append(prop[0]);
                
            }
            
            if (i < templateTokens.length - 1) s.append("/");
        }
        
        return s.toString();
    }
    
    private String getVarName(String t) {
        if (t.isEmpty()) return null;
        if (t.charAt(0) != '{' || t.charAt(t.length() - 1) != '}') return null;
        
        return t.substring(0 + 1, t.length() - 1);
    }
}
