package theweb.validation;

public class InvalidCharValidator implements Validator<InvalidChar, String> {
    @Override
    public Message validate(Object object, InvalidChar annotation, String property) {
        if (property != null) {
            for (int i = 0; i < property.length(); i ++)
                if (annotation.value().indexOf(property.charAt(i)) != -1)
                    return new Message(annotation.msg());
        }
        
        return null;
    }
}