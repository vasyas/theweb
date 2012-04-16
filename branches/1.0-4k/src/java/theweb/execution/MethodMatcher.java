package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import theweb.Page;

public interface MethodMatcher {
	public Method getMethod(Page page, HttpServletRequest request, Map<String, Object> properties);
}
