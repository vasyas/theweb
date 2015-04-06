package theweb;

public interface Response {
    void send(Page page, HttpExchange exchange) throws Exception;

    Response NotFound = (page, exchange) -> exchange.sendError(404);
    Response Unauthorized = (page, exchange) -> exchange.sendError(401);

    static Response error(int code) { return (page, exchange) -> exchange.sendError(code); }
    static Response error(int code, String msg) { return (page, exchange) -> exchange.sendError(code, msg == null ? "" + code : msg); }
}
