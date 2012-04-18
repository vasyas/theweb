package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import theweb.Action;
import theweb.HttpExchange;
import theweb.Page;
import theweb.PathPattern.Match;

public class NameMethodMatcher implements MethodMatcher {
	@Override
	public Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
        Match match = page.getPathPattern().match(exchange.getRequestPath());
        
        String methodName = match.remaining;
        
        while (methodName != null && methodName.endsWith("/"))
            methodName = methodName.substring(0, methodName.length() - 1);
        
        for (Method m1 : page.getClass().getMethods()) {
            if (matchActionName(methodName, m1)) return m1;
        }
        
        return null;
	}
	
    private boolean matchActionName(String methodName, Method method) {
        if (methodName == null) return false;
        
        if (methodName.equals(method.getName())) return true;
        
        Action annotation = method.getAnnotation(Action.class);
        if (annotation != null && methodName.equals(annotation.value())) return true;
        
        return false;
    }

}
