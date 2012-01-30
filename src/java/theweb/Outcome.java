package theweb;

public interface Outcome {
    void process(Page page, HttpExchange exchange) throws Exception;
}
