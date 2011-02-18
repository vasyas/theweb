package theweb.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import theweb.Page;

class MethodExecution implements Execution {
    public Method m;
    public Object[] args;
    public Page page;
    
    public Object execute() {
        try {
            if (!Modifier.isPublic(m.getModifiers())) 
                throw new RuntimeException("Action method " + m + " should be public");
                
            if (!m.isAccessible()) m.setAccessible(true);
            
            Object result = m.invoke(page, args);

            return result;
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
        return m;
    }

    @Override
    public Page getPage() {
        return page;
    }
}