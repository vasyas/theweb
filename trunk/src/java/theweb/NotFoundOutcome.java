package theweb;

public class NotFoundOutcome implements Outcome {
    @Override
    public void process(Page page, HttpExchange exchange) throws Exception {
        exchange.sendError(404);
    }

}
