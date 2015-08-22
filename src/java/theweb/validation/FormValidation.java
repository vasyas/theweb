package theweb.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class FormValidation {
    public static Map<Class<? extends Annotation>, Class<? extends Validator>> constraints = new HashMap<>();
    
    static {
        FormValidation.addConstraint(NotBlank.class, NotBlankValidator.class);
        FormValidation.addConstraint(Length.class, LengthValidator.class);
        FormValidation.addConstraint(Email.class, EmailValidator.class);
        FormValidation.addConstraint(Int.class, IntValidator.class);
        FormValidation.addConstraint(InvalidChar.class, InvalidCharValidator.class);
    }
    
    public static void addConstraint(Class<? extends Annotation> constraint, Class<? extends Validator> validator) {
        constraints.put(constraint, validator);
    }
    
    private void validateField(ValidationContext context, Object object, Field field, Class<? extends Validator> clazz, Annotation annotation) {
        try {
            Validator validator = clazz.newInstance();
            
            if (!field.isAccessible()) 
                field.setAccessible(true);
                
            Object property = field.get(object);
            
            Message message = validator.validate(object, annotation, property);
            
            if (message == null) return;
            
            context.addMessage(field.getName(), message);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void validateNestedObject(ValidationContext context, Object object, Field field) {
        try {
            if (!field.isAccessible()) 
                field.setAccessible(true);
                
            Object property = field.get(object);
            
            context = context.child(field.getName());
            
            validateObject(context, property);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void validateObject(ValidationContext context, Object object) {
        Class<?> clazz = object.getClass();
        
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                for (Annotation annotation : field.getAnnotations()) {
                    Class<? extends Validator> validator = null;
                    
                    if (annotation instanceof ValidatorClass) {
                        ValidatorClass validatorClass = (ValidatorClass) annotation;
                        
                        validator = validatorClass.value();
                    } else
                        validator = constraints.get(annotation.annotationType());
                    
                    if (validator != null)
                        validateField(context, object, field, validator, annotation);
                    
                    if (annotation instanceof Valid)
                        validateNestedObject(context, object, field);
                }
            }
            
            clazz = clazz.getSuperclass();
        }
        
        if (object instanceof CustomValidation)
            ((CustomValidation) object).validate(context);
    }
    
    private ValidationContext rootContext;
    
    public boolean isValidated() {
        return rootContext != null;
    }
    
    public boolean run(Object bean) {
        ValidationContext context = new ValidationContext();
        
        validateObject(context, bean);
        
        rootContext = context;
        
        return hasMessages();
    }

    public boolean hasMessages() {
        return !getMessages().isEmpty();
    }
    
    public Map<String, String> getMessages() {
        if (!isValidated()) return new HashMap<>();
        
        return rootContext.getMessages().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().asString(),
                        (a, b) -> a, // never the case
                        LinkedHashMap::new
                        ));
    }

    public String message() {
        return message(ValidationContext.GLOBAL);
    }

    public String message(String field) {
        return getMessages().getOrDefault(field, "");
    }

    public void add(String message) {
        if (rootContext == null) rootContext = new ValidationContext();

        rootContext.addGlobalMessage(message);
    }

    public void add(String field, String message) {
        if (rootContext == null) rootContext = new ValidationContext();

        rootContext.addMessage(field, message);
    }

    public ValidationContext getRootContext() {
        return rootContext;
    }
}
