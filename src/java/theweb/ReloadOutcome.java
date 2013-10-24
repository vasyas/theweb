package theweb;

import java.io.IOException;

public class ReloadOutcome implements Outcome {
    @Override
    public void process(Page page, HttpExchange exchange) throws IOException {
        exchange.sendRedirect(PageState.get().view());
    }
}
