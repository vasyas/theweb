package theweb;

import java.lang.reflect.Method;
import java.util.Map;

public class ViewMethodMatcher implements MethodMatcher {

	@Override
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
        for (Method method : page.getClass().getMethods()) {
            if (method.getAnnotation(View.class) != null) return method;
        }
        
        return null;
	}

}
