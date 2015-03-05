package theweb.validation;

public class NotBlankValidator implements Validator<NotBlank, String> {

    @Override
    public Message validate(Object object, NotBlank annotation, String property) {
        if (property == null || property.isEmpty()) return new Message(annotation.value());
        
        return null;
    }

}
