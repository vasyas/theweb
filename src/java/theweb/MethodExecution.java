package theweb;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Map;

class MethodExecution implements Execution {
    public Method method;
    public Object[] args;
    public Page page;

    public MethodExecution(Page page, Method method, Map<String, Object> properties) {
        this.page = page;
        this.method = method;
        this.args = getArgs(properties);
    }

    private Object[] getArgs(Map<String, Object> properties) {
        Object[] args = new Object[method.getParameters().length];

        for (int i = 0; i < args.length; i ++) {
            Parameter parameter = method.getParameters()[i];

            String requestPropertyName = null;

            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation instanceof Param) {
                    requestPropertyName = ((Param) annotation).value();
                    break;
                }
            }

            if (requestPropertyName == null && parameter.isNamePresent()) requestPropertyName = parameter.getName();

            if (requestPropertyName == null) continue;

            Object value = properties.get(requestPropertyName);

            if (value != null)
                args[i] = new TypeConvertor().convertValue(value, parameter.getType());
        }

        return args;
    }


    public Object execute() {
        try {
            if (!Modifier.isPublic(method.getModifiers()))
                throw new RuntimeException("Action method " + method + " should be public");
                
            if (!method.isAccessible()) method.setAccessible(true);

            return method.invoke(page, args);
        } catch (RuntimeException e) {
            throw e;
        } catch(InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException)
                throw (RuntimeException) e.getTargetException();
            
            throw new RuntimeException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Page getPage() {
        return page;
    }
    
    @Override
    public Object[] getArgs() {
        return args;
    }
}