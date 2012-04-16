package theweb.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** 
 * Field of bean, marked with this annotation, will be scanned for nested validation
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {
	String value() default "validation.error";
}
