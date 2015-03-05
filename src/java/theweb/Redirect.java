package theweb;

import java.io.IOException;

public class Redirect implements Response {
    private final String to;

    public Redirect(String to) {
        this.to = to;
    }
    
    public Redirect(Page page) {
        this(new PageState(page).view());
    }
    
    public Redirect(Page page, String hash) {
        this(new PageState(page).view() + "#" + hash);
    }
    
    @Override
    public void send(Page page, HttpExchange exchange) throws IOException {
        exchange.sendRedirect(to);
    }
}
