package theweb;

import theweb.velocity.Template;

public class DefaultCastInterceptor implements PageInterceptor {

    @Override
    public Response execute(Execution execution, HttpExchange exchange) throws Exception {
        Object result = execution.execute();

        if (result instanceof Response)
            return (Response) result;
        
        if (result instanceof String)
            return new ContentResponse((String) result, "text/html; charset=UTF-8");

        if (result instanceof Template)
            return new ContentResponse(((Template) result).render(), "text/html; charset=UTF-8");
        
        if (result == null)
        	return new NoResponse();
        
        return new Redirect(execution.getPage());
    }
}
