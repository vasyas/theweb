package theweb;

import java.io.IOException;

public class NoResponse implements Response {
    @Override
    public void send(Page page, HttpExchange exchange) throws IOException {
    	exchange.getWriter().close();
    }
}
