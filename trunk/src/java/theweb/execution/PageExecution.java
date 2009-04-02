package theweb.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import theweb.ContentOutcome;
import theweb.Outcome;
import theweb.Page;
import theweb.ReloadOutcome;

class PageExecution implements Execution {
    public Method m;
    public Object[] args;
    public Page page;
    
    public Outcome execute() {
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

    @Override
    public Method getMethod() {
        return m;
    }

    @Override
    public Page getPage() {
        return page;
    }
}