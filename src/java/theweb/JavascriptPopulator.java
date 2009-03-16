package theweb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class JavascriptPopulator implements Populator {
    public void populate(Page page, Map<String, Object> properties) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        
        Bindings bindings = jsEngine.createBindings();
        bindings.put("page", page);
        bindings.put("properties", properties);
        
        for (String key : properties.keySet()) {
            try {
                Object value = properties.get(key);
                
                // convert array with length=1 to single element, if receiving property is not an array
                if (key.indexOf('.') != -1 && value.getClass().isArray()) {
                    String parentObjPath = key.substring(0, key.indexOf('.'));
                    Object parentObj = jsEngine.eval(parentObjPath, bindings);
                    
                    try {
                        Field f = parentObj.getClass().getField(key.substring(key.indexOf('.') + 1));
                        
                        if (!f.getType().isArray() && Array.getLength(value) == 1) {
                            value = Array.get(value, 0);
                            properties.put(key, value);
                        }
                    } catch (SecurityException e) {
                    } catch (NoSuchFieldException e) {
                    }
                }
                
                String s = key + " = properties.get('" + key + "')";
                
                jsEngine.eval(s, bindings);
            } catch (ScriptException ex) {
                // log.debug("Not populated (this is not an error)", ex);
            }    
        }
    }
}