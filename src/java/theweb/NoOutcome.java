package theweb;

import java.io.IOException;

public class NoOutcome implements Outcome {
    @Override
    public void process(Page page, HttpExchange exchange) throws IOException {
    }
}
