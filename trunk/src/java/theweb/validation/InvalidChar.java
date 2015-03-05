package theweb.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InvalidChar {
    String value();
    String msg() default "validation.error.invalidchar";
}
