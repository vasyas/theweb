package theweb.validation;

import java.lang.annotation.Annotation;

public interface Validator<AnnotationType extends Annotation, PropertyType> {
    Message validate(Object object, AnnotationType annotation, PropertyType property);
}
