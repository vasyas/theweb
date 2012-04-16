package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import theweb.Page;

public class NameMethodMatcher implements MethodMatcher {
	@Override
	public Method getMethod(Page page, HttpServletRequest request, Map<String, Object> properties) {
        String path = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        
        String methodName = null;
        
        int idx = path.lastIndexOf('/');
        
        if (idx != -1 && idx != path.length() - 1)
            methodName = path.substring(idx + 1);
        
        for (Method m1 : page.getClass().getMethods()) {
            if (m1.getName().equals(methodName)) return m1;
        }
        
        return null;
	}
}
