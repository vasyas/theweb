package theweb.execution;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.ContentOutcome;
import theweb.Markup;
import theweb.Outcome;
import theweb.RenderOutcome;

public class DefaultCastInterceptor implements PageInterceptor {

    @Override
    public Outcome execute(Execution execution, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object result = execution.execute();
        
        if (result instanceof Outcome) 
            return (Outcome) result;
        
        if (result instanceof String)
            return new ContentOutcome((String) result, "text/html; charset=UTF-8");
        
        if (result instanceof Markup)
            return new ContentOutcome(((Markup) result).render(), "text/html; charset=UTF-8");
        
        return new RenderOutcome();
    }
}
