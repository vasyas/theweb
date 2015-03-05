package theweb;

public interface PageInterceptor {
    Object execute(Execution execution, HttpExchange exchange) throws Exception;
}
