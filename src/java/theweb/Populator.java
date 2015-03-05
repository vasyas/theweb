package theweb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Populator {
    public void populate(Page page, Map<String, Object> properties) {
        for (String key : properties.keySet()) {
            Object value = properties.get(key);
            
            key = key.substring(key.indexOf('.') + 1);
            
            evaluate(page, key, value); 
        }
    }
    
    Pattern mapRegex = Pattern.compile("([\\w_\\$]+)\\[([\\w_\\$]+)\\]");
    
    @SuppressWarnings("unchecked")
    private Class<?>[] getMapParams(Map map) {
        Class<?> cls = map.getClass();
        
        while (cls != null) {
            Type type = cls.getGenericSuperclass();
            
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                
                if (parameterizedType.getRawType() instanceof Class &&
                        java.util.Map.class.isAssignableFrom((Class) parameterizedType.getRawType())) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    
                    if (actualTypeArguments[0] instanceof Class && actualTypeArguments[1] instanceof Class) {
                        return new Class[] {
                                (Class) actualTypeArguments[0],
                                (Class) actualTypeArguments[1]
                        };
                    }
                }
            }
            
            cls = cls.getSuperclass();
        }
        
        return null;
    }
    
    // Only field references to map are supported. Bean properties not supported
    // Type conversions for map is not supported
    @SuppressWarnings("unchecked")
    private void evaluateKeyCheck(Object current, String path, Object key, Object value) {
        if (key != null) { // should retreive map object
            Map map = (Map) current;
            
            // try to convert key & value to actual parameters
            Class<?>[] mapParams = getMapParams(map);
            
            if (mapParams != null)
                key = new TypeConvertor().convertValue(key, mapParams[0]);
            
            if (path == null) { // just set object
                if (mapParams != null)
                    value = new TypeConvertor().convertValue(value, mapParams[1]);
                
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
        
        // try public field
        try {
            Field field = current.getClass().getField(component);
            
            if (Modifier.isPublic(field.getModifiers())) {
                if (!field.isAccessible()) field.setAccessible(true); // access inner classes
                
                if (path == null && key == null) { // no next component
                    value = new TypeConvertor().convertValue(value, field.getType());
                    
                    try {
                        field.set(current, value);
                    } catch (IllegalArgumentException | IllegalAccessException ignored) {
                    }

                    return;
                }
                
                Object next = field.get(current);
                
                evaluateKeyCheck(next, path, key, value);
            }
        } catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
        }

        // try property accessor
        if (path == null && key == null) { // looking for setProperty
            String methodName = "set" + toCapitalCase(component);
            
            try {
                Method method = getMethodOfName(current.getClass(), methodName);
                
                if (method == null || !Modifier.isPublic(method.getModifiers())) return;
                
                if (!method.isAccessible()) method.setAccessible(true);
                
                Class<?> propertyType = method.getParameterTypes()[0];
                
                value = new TypeConvertor().convertValue(value, propertyType);
                
                method.invoke(current, value);
            } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
            }

            return;
        } else { // looking for getProperty
            String methodName = "get" + toCapitalCase(component);

            try {
                Method method = current.getClass().getMethod(methodName);
                Object next = method.invoke(current);
                
                evaluateKeyCheck(next, path, key, value);
                return;
            } catch (SecurityException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
            }
        }
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