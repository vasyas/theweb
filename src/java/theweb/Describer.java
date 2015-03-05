package theweb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

class Describer {
    private final Map<String, String[]> parameterMap;

    public Describer(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public void describe(Page page) {
        try {
            describe(page, "page.");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
    
    private void describe(Object page, String prefix) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        describeDeclaredFields(page, prefix);
        describeCustom(page.getClass(), page);
    }

    private void describeCustom(Class<?> type, Object object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (type.getSuperclass() != null) describeCustom(type.getSuperclass(), object);

        for (Method m : type.getDeclaredMethods())
            if (m.getAnnotation(CustomDescription.class) != null)
                m.invoke(object, parameterMap);
    }

    @SuppressWarnings("unchecked")
    private void describeDeclaredFields(Object object, String prefix) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Field field : object.getClass().getFields()) {
            if (!field.isAccessible()) field.setAccessible(true);
            
            if (Modifier.isTransient(field.getModifiers())) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
            
            Object value = field.get(object);
            
            if (value != null && field.getAnnotation(Describe.class) != null) {
                describe(value, prefix + field.getName() + ".");
                continue;
            }

            if (value instanceof Map) {
                describeMap(prefix + field.getName(), (Map<String, Object>) value);
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

    public void describeMap(String mapName, Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);

            String convertedValue = (String) new TypeConvertor().convertValue(value, String.class);

            if (convertedValue != null && !convertedValue.isEmpty())
                parameterMap.put(mapName + "[" + key + "]", new String[] { convertedValue });
        }
    }
}
