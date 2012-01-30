package theweb;

import java.io.IOException;

public class RedirectOutcome implements Outcome {
    private final String to;

    public RedirectOutcome(String to) {
        this.to = to;
    }
    
    public RedirectOutcome(Page page) {
        this(new PageState(page).view());
    }
    
    public RedirectOutcome(Page page, String hash) {
        this(new PageState(page).view() + "#" + hash);
    }
    
    @Override
    public void process(Page page, HttpExchange exchange) throws IOException {
        exchange.sendRedirect(to);
    }

}
