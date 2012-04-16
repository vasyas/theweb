package theweb.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Length {
	int min() default 0;
	int max() default 0;
	String minMsg() default "validation.error.length.min";
	String maxMsg() default "validation.error.length.max";
}
