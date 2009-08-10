package theweb.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import theweb.ContentOutcome;
import theweb.Markup;
import theweb.Outcome;
import theweb.Page;
import theweb.RenderOutcome;

class PageExecution implements Execution {
    public Method m;
    public Object[] args;
    public Page page;
    
    public Outcome execute() {
        try {
            if (!Modifier.isPublic(m.getModifiers())) 
                throw new RuntimeException("Action method " + m + " should be public");
                
            if (!m.isAccessible()) m.setAccessible(true);
            
            Object result = m.invoke(page, args);
            
            if (result instanceof Outcome) 
                return (Outcome) result;
            
            if (result instanceof String)
                return new ContentOutcome((String) result, "text/html; charset=UTF-8");
            
            if (result instanceof Markup)
                return new ContentOutcome(((Markup) result).render(), "text/html; charset=UTF-8");
            
            return new RenderOutcome();
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