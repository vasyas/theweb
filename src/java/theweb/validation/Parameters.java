package theweb.validation;

import java.lang.annotation.Annotation;

public interface Parameters<T extends Annotation> {
	void init(T t);
}
