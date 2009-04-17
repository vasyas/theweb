package theweb.execution;

import java.lang.reflect.Array;

public class TypeConvertor {
    @SuppressWarnings("unchecked")
    public Object convertValue(Object value, Class<?> toClass) {
        if (value == null) return null;
        
        if (toClass.isAssignableFrom(value.getClass())) return value;
        
        if (value instanceof String) {
            if (String.class.equals(toClass)) return value;
            
            if (Integer.class.equals(toClass) || Integer.TYPE.equals(toClass))
                return Integer.parseInt((String) value);
            
            if (Boolean.class.equals(toClass) || Boolean.TYPE.equals(toClass))
                return "true".equals(value);
            
            if (toClass.isEnum())
                return Enum.valueOf((Class<? extends Enum>) toClass, (String) value);
        }
        
        if (value.getClass().isArray() && toClass.isAssignableFrom(value.getClass().getComponentType()))
            return Array.get(value, 0);
        
        if (toClass.isArray() && toClass.getComponentType().isAssignableFrom(value.getClass())) {
            Object a = Array.newInstance(toClass.getComponentType(), 1);
            Array.set(a, 0, value);
            return a;
        }
        
        if (value instanceof Integer && toClass.equals(String.class))
            return "" + value;
        
        if (value instanceof Boolean && toClass.equals(String.class))
            return "" + value;
        
        // try to convert component types
        if (value.getClass().isArray() && !toClass.isArray()) {
            Object element = null;
            
            if (Array.getLength(value) > 0)
                element = Array.get(value, 0);
                
            return convertValue(element, toClass);
        }
        
        if (!value.getClass().isArray() && toClass.isArray()) {
            Object element = convertValue(value, toClass.getComponentType());
            
            if (element != null) {
                Object a = Array.newInstance(toClass.getComponentType(), 1);
                Array.set(a, 0, element);
                return a;
            }
        }
        
        if (value.getClass().isArray() && toClass.isArray()) {
            Object a = Array.newInstance(toClass.getComponentType(), Array.getLength(value));
            
            for (int i = 0; i < Array.getLength(value); i ++)
                Array.set(a, i, convertValue(Array.get(value, i), toClass.getComponentType()));
            
            return a;
        }
        
        // no conversion available 
        return null;
    }
}