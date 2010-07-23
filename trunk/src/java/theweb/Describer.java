package theweb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import theweb.execution.TypeConvertor;

class Describer {
    private final Map<String, String[]> parameterMap;

    public Describer(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public void describe(Page page) {
        try {
            describe(page, "page.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
    
    private void describe(Object page, String prefix) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        describeDeclaredFields(page, prefix);
        describeCustom(page);
    }

    private void describeCustom(Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Method m : object.getClass().getMethods())
            if (m.getAnnotation(CustomDescription.class) != null)
                m.invoke(object, parameterMap);
    }
    
    private void describeDeclaredFields(Object object, String prefix) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Field field : object.getClass().getFields()) {
            if (!field.isAccessible()) field.setAccessible(true);
            
            if (Modifier.isTransient(field.getModifiers())) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
            
            Object value = field.get(object);
            
            if (field.getAnnotation(Describe.class) != null) {
                describe(value, prefix + field.getName() + ".");
                continue;
            }
            
            String[] result = null;
            
            if (field.getType().isArray()) {
                Object[] array = (Object[]) field.get(object);
                result = new String[array.length];
                
                for (int i = 0; i < array.length; i ++) {
                    String convertedValue = (String) new TypeConvertor().convertValue(array[i], String.class);
                    
                    result[i] = convertedValue;
                }
            } else {
                String convertedValue = (String) new TypeConvertor().convertValue(value, String.class);
                
                if (convertedValue != null && !convertedValue.isEmpty())
                    result = new String[] { convertedValue };
            }
            
            if (result != null)
                parameterMap.put(prefix + field.getName(), result);
        }
    }
}
