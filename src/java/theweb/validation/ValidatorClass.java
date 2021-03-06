package theweb.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatorClass {
    @SuppressWarnings("unchecked") Class<? extends Validator> value();
}
