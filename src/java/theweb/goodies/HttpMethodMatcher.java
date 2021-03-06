package theweb.goodies;

import theweb.HttpExchange;
import theweb.MethodMatcher;
import theweb.Page;

import java.lang.reflect.Method;
import java.util.Map;

public class HttpMethodMatcher implements MethodMatcher {

	@Override
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
        for (Method method : page.getClass().getMethods()) {
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            
            if (httpMethod != null && exchange.getRequestMethod().equals(httpMethod.value()))
                return method;
        }
        
        return null;
	}

}
