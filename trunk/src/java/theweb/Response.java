package theweb;

public interface Response {
    void send(Page page, HttpExchange exchange) throws Exception;

    Response NotFound = (page, exchange) -> exchange.sendError(404);
}
