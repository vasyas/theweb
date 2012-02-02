package theweb.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Validation {
	public static Map<Class<? extends Annotation>, Class<? extends Validator>> constraints = 
		new HashMap<Class<? extends Annotation>, Class<? extends Validator>>();
    
	static {
        Validation.addConstraint(Login.class, LoginValidator.class);
        Validation.addConstraint(Email.class, EmailValidator.class);
		Validation.addConstraint(Length.class, LengthValidator.class);
		Validation.addConstraint(InvalidChar.class, InvalidCharValidator.class);
		Validation.addConstraint(NotBlank.class, NotBlankValidator.class);
		Validation.addConstraint(Valid.class, ValidValidator.class);
	}
	
	public static void addConstraint(Class<? extends Annotation> constraint, Class<? extends Validator> validator) {
		constraints.put(constraint, validator);
	}

	@SuppressWarnings("unchecked")
	static void validate(Object object, Field field, Class<? extends Validator> clazz, Annotation annotation) {
		try {
			Validator validator = clazz.newInstance();
			
			if (validator instanceof Parameters) {
				Parameters<Annotation> parameters = (Parameters<Annotation>) validator;
				parameters.init(annotation);
			}
			
			if (!field.isAccessible()) 
			    field.setAccessible(true);
			    
			Object property = field.get(object);
			
			validator.validate(object, property);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void validate(Object bean) {
		for (Field field : bean.getClass().getFields()) {
			for (Annotation annotation : field.getAnnotations()) {
				Class<? extends Validator> clazz = null;
				
				if (annotation instanceof ValidatorClass) {
					ValidatorClass validatorClass = (ValidatorClass) annotation;
					
					clazz = validatorClass.value();
				} else
					clazz = constraints.get(annotation.annotationType());
				
				if (clazz != null)
					validate(bean, field, clazz, annotation);
			}
		}
		
		if (bean instanceof CustomValidation) {
			CustomValidation validation = (CustomValidation) bean;
			
			validation.validate();
		}
	}
}
