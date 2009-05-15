package theweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectOutcome implements Outcome {
    private final String to;

    public RedirectOutcome(String to) {
        this.to = to;
    }
    
    public RedirectOutcome(Page page) {
        this(new PageState(page).view());
    }
    
    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(to);
    }

}
