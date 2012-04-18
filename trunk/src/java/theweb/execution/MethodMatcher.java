package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import theweb.HttpExchange;
import theweb.Page;

public interface MethodMatcher {
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties);
}
