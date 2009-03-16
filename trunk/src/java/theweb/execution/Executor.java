package theweb.execution;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.DefaultAction;
import theweb.Outcome;
import theweb.Page;
import theweb.Param;
import theweb.RenderOutcome;

public class Executor {
    private final List<PageInterceptor> interceptors;

    public Executor(List<PageInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
    
    public Outcome exec(Page page, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        
        String method = null;
        
        int idx = path.lastIndexOf('/');
        
        if (idx != -1 && idx != path.length() - 1)
            method = path.substring(idx + 1);
        
        Execution pageExecution = getExecution(request, page, method);
        
        ChainedActionExecution chainedActionInvokation = new ChainedActionExecution(interceptors, request, response, pageExecution);

        return chainedActionInvokation.execute();
    }

    private Execution getExecution(HttpServletRequest request, final Page page, String name) {
        Method m = null;
        
        for (Method m1 : page.getClass().getMethods()) {
            if (m1.getName().equals(name))
                m = m1;
        }
        
        if (m == null)
            for (Method m1 : page.getClass().getMethods()) {
                if (m1.getAnnotation(DefaultAction.class) != null)
                    m = m1;
            }

        if (m != null) {
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
            
            PageExecution invocation = new PageExecution();
            invocation.m = m;
            invocation.args = args;
            invocation.page = page;
            
            return invocation;
        }
        
        // default execution - just render a page
        
        return new Execution() { 
            @Override
            public Outcome execute() {
                return new RenderOutcome();
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Page getPage() {
                return page;
            }
        };
    }
}
