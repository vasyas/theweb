package theweb;

import java.util.HashMap;
import java.util.Map;

public class PathPattern {
    public final String pattern;

    public PathPattern(String pattern) {
        if (!pattern.endsWith("/"))
            throw new IllegalStateException("Page urls should ends with /");
        
        this.pattern = pattern;
    }
    
    // templateTokens = []
    // pathTokens = [, method]
    
    // templateTokens = [, report]
    // pathTokens = [, report, method]
    
    public Match match(String path) {
        String templateTokens[] = pattern.split("/");
        String pathTokens[] = path.split("/");
        
        Map<String, String> vars = new HashMap<String, String>();
        
        int remainingStart = 1;
        
        for (int i = 0; i < templateTokens.length; i ++) {
            String varName = getVarName(templateTokens[i]);
            
            if (varName == null) // not variable - should be equal
                if (pathTokens.length <= i || !pathTokens[i].equals(templateTokens[i])) 
                    return new Match();
            
            if (i < pathTokens.length) {
                if (varName != null) vars.put(varName, pathTokens[i]);
                
                remainingStart += 1 + pathTokens[i].length();
            }
        }
        
        String remaining = null;
        
        if (remainingStart < path.length()) 
            remaining = path.substring(remainingStart);
        
        return new Match(vars, remaining);
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
    
    public class Match {
        private final Map<String, String> vars;
        public final String remaining;
        
        private Match(Map<String, String> vars, String remaining) {
            this.vars = vars;
            this.remaining = remaining;
        }
        
        private Match() {
            this(null, null);
        }
        
        public boolean matched() {
            return vars != null;
        }
        
        public String get(String name) {
            if (!matched()) throw new IllegalStateException();
            
            return vars.get(name);
        }

        public Map<String, String> getVars() {
            return vars;
        }
    }
}
