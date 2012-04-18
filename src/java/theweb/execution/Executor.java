package theweb.execution;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import theweb.HttpExchange;
import theweb.Outcome;
import theweb.Page;
import theweb.Param;
import theweb.RenderOutcome;

public class Executor {
	private final List<MethodMatcher> methodMatchers;
    private final List<PageInterceptor> interceptors;

    public Executor(List<MethodMatcher> methodMatchers, List<PageInterceptor> interceptors) {
        this.methodMatchers = methodMatchers;
        this.interceptors = interceptors;
    }
    
    public Object exec(Page page, Map<String, Object> properties, HttpExchange exchange) throws IOException {
        Execution pageExecution = getExecution(page, exchange, properties);
        
        ChainedActionExecution chainedActionInvokation = new ChainedActionExecution(interceptors, exchange, pageExecution);

        return chainedActionInvokation.execute();
    }

    private Method findMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
    	for (MethodMatcher methodMatcher : methodMatchers) {
    		Method method = methodMatcher.getMethod(page, exchange, properties);
    		
    		if (method != null) return method;
    	}
    	
    	return null;
    }
    
    private Execution getExecution(final Page page, HttpExchange exchange, Map<String, Object> properties) {
        Method m = findMethod(page, exchange, properties);

        if (m != null) {
            Object[] args = new Object[m.getParameterTypes().length];
            
            // try to find required parameters
            // method parameters can be either InvocationContext or marked with @Param annotation
            for (int i = 0; i < m.getParameterTypes().length; i ++) {
                for (Annotation annotation  : m.getParameterAnnotations()[i]) {
                    if (annotation  instanceof Param) {
                        Param paramAnnotation = (Param) annotation;
                        
                        // find request parameter
                        Object value = properties.get(paramAnnotation.value());
                        
                        if (value != null)
                            args[i] = new TypeConvertor().convertValue(value, m.getParameterTypes()[i]);
                    }
                }
            }
            
            MethodExecution invocation = new MethodExecution();
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

            @Override
            public Object[] getArgs() {
                return null;
            }
        };
    }
}
