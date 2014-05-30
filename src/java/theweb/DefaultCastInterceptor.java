package theweb;

import theweb.execution.Execution;
import theweb.execution.PageInterceptor;

import java.io.IOException;

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
        
        if (execution.getMethod().getAnnotation(DefaultAction.class) != null)
            return new RenderOutcome();
        
        if (result == null)
        	return new NoOutcome();
        
        return new RedirectOutcome(execution.getPage());
    }
}
