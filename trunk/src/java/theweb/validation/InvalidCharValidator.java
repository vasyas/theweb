package theweb.validation;

import theweb.Messages;

public class InvalidCharValidator extends ParametrizedValidator<InvalidChar> implements Validator<String> {
	public void validate(Object object, String property) {
		if (property != null) {
			for (int i = 0; i < property.length(); i ++)
				if (annotation.value().indexOf(property.charAt(i)) != -1) {
					Messages.error(annotation.msg());
					return;
				}
		}
	}
}