package theweb.validation;

public class ValidValidator extends ParametrizedValidator<Valid> implements Validator<Object> {
	public void validate(Object object, Object bean) {
		Validation.validate(bean);
	}
}
