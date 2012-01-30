package theweb.validation;

import java.io.IOException;
import java.lang.reflect.Method;

import theweb.HttpExchange;
import theweb.execution.Execution;
import theweb.execution.PageInterceptor;

public class ValidatingInterceptor implements PageInterceptor {
    
    @Override
    public Object execute(Execution execution, HttpExchange exchange) throws IOException {
        Method method = execution.getMethod();
        
        if (execution.getPage().getClass().getAnnotation(Validate.class) != null || (method != null && method.getAnnotation(Validate.class) != null))
            Validation.validate(execution.getPage());
        
        return execution.execute();
    }
}
