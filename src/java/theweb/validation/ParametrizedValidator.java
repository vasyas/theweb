package theweb.validation;

import java.lang.annotation.Annotation;

public class ParametrizedValidator<T extends Annotation> implements Parameters<T> {
	protected T annotation;
	
	public void init(T annotation) {
		this.annotation = annotation;
	}
}