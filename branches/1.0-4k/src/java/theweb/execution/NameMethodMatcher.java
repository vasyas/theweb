package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import theweb.Page;
import theweb.PathPattern.Match;

public class NameMethodMatcher implements MethodMatcher {
	@Override
	public Method getMethod(Page page, HttpServletRequest request, Map<String, Object> properties) {
        String path = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        
        Match match = page.getPathPattern().match(path);
        
        String methodName = match.remaining;
        
        while (methodName.endsWith("/"))
            methodName = methodName.substring(0, methodName.length() - 1);
        
        for (Method m1 : page.getClass().getMethods()) {
            if (m1.getName().equals(methodName)) return m1;
        }
        
        return null;
	}
}
