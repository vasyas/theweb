package theweb.execution;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import theweb.Page;

public class HttpMethodMatcher implements MethodMatcher {

	@Override
	public Method getMethod(Page page, HttpServletRequest request, Map<String, Object> properties) {
        for (Method method : page.getClass().getMethods()) {
            HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
            
            if (httpMethod != null && request.getMethod().equals(httpMethod.value()))
                return method;
        }
        
        return null;
	}

}
