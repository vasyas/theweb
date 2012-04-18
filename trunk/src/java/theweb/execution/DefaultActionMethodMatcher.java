package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import theweb.DefaultAction;
import theweb.HttpExchange;
import theweb.Page;

public class DefaultActionMethodMatcher implements MethodMatcher {

	@Override
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
        for (Method method : page.getClass().getMethods()) {
            if (method.getAnnotation(DefaultAction.class) != null) return method;
        }
        
        return null;
	}

}
