package theweb;

import java.io.IOException;

public class ReloadResponse implements Response {
    @Override
    public void send(Page page, HttpExchange exchange) throws IOException {
        exchange.sendRedirect(PageState.get().view());
    }
}
