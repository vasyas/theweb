package theweb.sessions;

import com.sun.net.httpserver.HttpExchange;
import theweb.goodies.Cookies;

import java.io.IOException;

public class SessionFilter extends com.sun.net.httpserver.Filter {
    private SessionManager sessionManager;
    
    public SessionFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    @Override
    public String description() {
        return "SessionFilter";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Cookies cookies = new Cookies(exchange);

        String id = cookies.get("sessionid");
        
        Session session = sessionManager.getOrCreateSession(id);
        
        cookies.add("sessionid", session.id);
        
        try {
            Session.set(session);
            
            chain.doFilter(exchange);
        } finally {
            Session.set(null);
        }
    }
}
