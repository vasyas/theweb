package theweb;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

class Executor {
    public Outcome exec(Page page, HttpServletRequest request) {
        String path = request.getPathInfo();
        
        int idx = path.indexOf('/', 1);
        
        if (idx == -1) return new RenderOutcome();
        if (idx == path.length() - 1) return new RenderOutcome();
        
        String method = path.substring(idx + 1);
        
        Invocation invocation = getInvocation(request, page, method);
        
        if (invocation == null) return new RenderOutcome();
        
        return invocation.invoke();
    }

    private Invocation getInvocation(HttpServletRequest request, Page page, String name) {
        for (Method m : page.getClass().getMethods()) {
            if (m.getName().equals(name)) {
                Object[] args = new Object[m.getParameterTypes().length];
                
                // try to find required parameters
                // method parameters can be either InvocationContext or marked with @Param annotation
                for (int i = 0; i < m.getParameterTypes().length; i ++) {
                    for (Annotation annotation  : m.getParameterAnnotations()[i]) {
                        if (annotation  instanceof Param) {
                            Param paramAnnotation = (Param) annotation;
                            
                            // find request parameter
                            String value = request.getParameter(paramAnnotation.value());
                            
                            if (value != null)
                                args[i] = value.trim();
                        }
                    }
                }
                
                Invocation invocation = new Invocation();
                invocation.m = m;
                invocation.args = args;
                invocation.page = page;
                
                return invocation;
            }
        }
        
        return null;
    }
    
    private static class Invocation {
        public Method m;
        public Object[] args;
        public Page page;
        
        public Outcome invoke() {
            try {
                Object result = m.invoke(page, args);
                
                if (result instanceof Outcome) 
                    return (Outcome) result;
                
                if (result instanceof String)
                    return new ContentOutcome((String) result, "text/html; charset=UTF-8");
                
                return new ReloadOutcome();
            } catch(InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
