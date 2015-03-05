package theweb.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
    String value() default "validation.error.blank";
}
