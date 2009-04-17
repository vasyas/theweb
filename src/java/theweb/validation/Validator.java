package theweb.validation;

public interface Validator<T> {
	void validate(Object object, T property);
}
