package theweb.validation;

import theweb.Messages;

public class LengthValidator extends ParametrizedValidator<Length> implements Validator<String> {
	public void validate(Object object, String property) {
		if (property == null) {
			if (annotation.min() > 0)
				Messages.error(annotation.minMsg(), annotation.min());
		} else {
			if (property.length() < annotation.min())
				Messages.error(annotation.minMsg(), annotation.min());
			
			if (property.length() > annotation.max())
				Messages.error(annotation.maxMsg(), annotation.max());
		}
	}
}