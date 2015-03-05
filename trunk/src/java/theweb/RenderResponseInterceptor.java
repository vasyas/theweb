package theweb;

public class RenderResponseInterceptor implements PageInterceptor {
    public Object execute(Execution execution, HttpExchange exchange) throws Exception {
        Object execute = execution.execute();

        if (execute instanceof Response) {
            ((Response) execute).send(execution.getPage(), exchange);

            return null;
        }

        return execute;
    }
}
