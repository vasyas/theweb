package theweb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import theweb.execution.TypeConvertor;

public class ReflectionPopulator implements Populator {
    private final boolean stripParentObjectName;

    public ReflectionPopulator(boolean stripParentObjectName) {
        this.stripParentObjectName = stripParentObjectName;
    }
    
    public void populate(Page page, Map<String, Object> properties) {
        for (String key : properties.keySet()) {
            Object value = properties.get(key);
            
            if (stripParentObjectName)
                key = key.substring(key.indexOf('.') + 1);
            
            evaluate(page, key, value); 
        }
    }
    
    Pattern mapRegex = Pattern.compile("([\\w_\\$]+)\\[([\\w_\\$]+)\\]");
    
    // Only field references to map are supported. Bean properties not supported
    // Type conversions for map is not supported
    @SuppressWarnings("unchecked")
    private void evaluateKeyCheck(Object current, String path, String key, Object value) {
        if (key != null) { // should retreive map object
            Map<String, Object> map = (Map<String, Object>) current;
            
            if (path == null) { // just set object
                map.put(key, value);
                return;
            } else { // continue parsing
                current = map.get(key);
                
                if (current == null) return;
            }
        }
        
        evaluate(current, path, value);
    }
    
    private void evaluate(Object current, String path, Object value) {
        String component;
        
        int idx = path.indexOf(".");
        if (idx == -1) {
            component = path;
            path = null;
        } else {
            component = path.substring(0, idx);
            path = path.substring(idx + 1);
        }
        
        Matcher matcher = mapRegex.matcher(component);
        
        String key = null;
        
        if (matcher.matches()) {
            component = matcher.group(1);
            key = matcher.group(2);
        }
        
        // try field
        try {
            Field field = current.getClass().getField(component);
            
            if (path == null && key == null) { // no next component
                value = new TypeConvertor().convertValue(value, field.getType());
                
                try {
                    field.set(current, value);
                } catch (IllegalArgumentException e) {
                } catch(IllegalAccessException e) {
                }
                
                return;
            }
            
            Object next = field.get(current);
            
            evaluateKeyCheck(next, path, key, value);
        } catch(NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        
        // try property accessor
        if (path == null && key == null) { // looking for setProperty
            String methodName = "set" + toCapitalCase(component);
            
            try {
                Method method = getMethodOfName(current.getClass(), methodName);
                
                if (method == null) return;
                
                Class<?> propertyType = method.getParameterTypes()[0];
                
                value = new TypeConvertor().convertValue(value, propertyType);
                
                method.invoke(current, value);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            
            return;
        } else { // looking for getProperty
            String methodName = "get" + toCapitalCase(component);

            try {
                Method method = current.getClass().getMethod(methodName);
                Object next = method.invoke(current);
                
                evaluateKeyCheck(next, path, key, value);
                return;
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        
        // not found otherwise
        return;
    }

    private Method getMethodOfName(Class<?> clazz, String methodName) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName)) return m; 
        }
        
        return null;
    }

    private String toCapitalCase(String component) {
        if (component.isEmpty()) return "";
        
        if (component.length() == 1) return component;
        
        return Character.toUpperCase(component.charAt(0)) + component.substring(1);
    }
}