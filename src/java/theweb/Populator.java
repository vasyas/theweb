package theweb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

class Populator {
    @SuppressWarnings("unchecked")
    public void populate(Page page, HttpServletRequest request) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        
        // trim all Strings
        for (Map.Entry entry : (Set<Map.Entry>)request.getParameterMap().entrySet()) {
            String name = (String) entry.getKey();
            Object value = entry.getValue();

            if ( value instanceof String )
                properties.put(name, ((String)value).trim());
            else if ( value instanceof String[]) {
                String[] strings = (String[]) entry.getValue();

                for (int i = 0; i < strings.length; i ++)
                    strings[i] = strings[i].trim();

                properties.put(name, strings);
            } else
                properties.put(name, value);
        }
        
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