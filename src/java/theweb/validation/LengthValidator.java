package theweb.validation;

public class LengthValidator implements Validator<Length, String> {
    @Override
    public Message validate(Object object, Length annotation, String property) {
        if (annotation.blank() && (property == null || property.isEmpty()))
            return null;
        
        if (property == null) {
            if (annotation.min() > 0)
                return new Message(annotation.minMsg(), annotation.min());
        } else {
            if (annotation.min() != 0 && property.length() < annotation.min())
                return new Message(annotation.minMsg(), annotation.min());
            
            if (annotation.max() != 0 && property.length() > annotation.max())
                return new Message(annotation.maxMsg(), annotation.max());
        }
        
        return null;
    }
}