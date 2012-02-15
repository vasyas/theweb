package theweb.execution;

import java.io.IOException;

import theweb.ContentOutcome;
import theweb.HttpExchange;
import theweb.Markup;
import theweb.Outcome;
import theweb.RedirectOutcome;

public class DefaultCastInterceptor implements PageInterceptor {

    @Override
    public Outcome execute(Execution execution, HttpExchange exchange) throws IOException {
        Object result = execution.execute();
        
        if (result instanceof Outcome) 
            return (Outcome) result;
        
        if (result instanceof String)
            return new ContentOutcome((String) result, "text/html; charset=UTF-8");
        
        if (result instanceof Markup)
            return new ContentOutcome(((Markup) result).render(), "text/html; charset=UTF-8");
        
        return new RedirectOutcome(execution.getPage());
    }
}
