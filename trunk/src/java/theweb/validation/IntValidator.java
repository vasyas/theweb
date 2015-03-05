package theweb.validation;

public class IntValidator implements Validator<Int, String> {
    @Override
    public Message validate(Object object, Int annotation, String property) {
        if (property == null || property.isEmpty())
            return null;
        
        try {
            Integer.parseInt(property);
        } catch(NumberFormatException e) {
            return new Message(annotation.value());
        }
        
        return null;
    }
}