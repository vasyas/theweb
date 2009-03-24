package theweb.execution;

public class TypeConvertor {

    public Object convertValue(String value, Class<?> toClass) {
        if (String.class.equals(toClass)) return value;
        
        if (Integer.class.equals(toClass))
            return Integer.parseInt(value);
        
        return null;
    }

}
