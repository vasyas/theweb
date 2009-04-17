package theweb.validation;

import theweb.Messages;

public class NotBlankValidator extends ParametrizedValidator<NotBlank> implements Validator<String> {

	public void validate(Object object, String property) {
		if (property == null || property.isEmpty()) Messages.error(annotation.value());
	}

}
