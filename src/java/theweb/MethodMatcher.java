package theweb;

import java.lang.reflect.Method;
import java.util.Map;

public interface MethodMatcher {
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties);
}
