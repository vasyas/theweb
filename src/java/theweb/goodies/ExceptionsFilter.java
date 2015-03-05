package theweb.goodies;

import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionsFilter extends com.sun.net.httpserver.Filter {
    private final static Logger log = Logger.getLogger(ExceptionsFilter.class);
    
    @Override
    public String description() {
        return "ExceptionsFilter";
    }
    
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        try {
            chain.doFilter(exchange);
        } catch(Exception e) {
        	String s = "";
        	
        	if (e.getMessage() != null) s = e.getMessage();
        	
			if (s.contains("Broken pipe") || s.contains("Connection reset by peer")) return;
        	
            log.error("Can't handle request " + exchange.getRequestURI(), e);
            
            dumpException(exchange, e);
        } finally {
            exchange.close();
        }
    }
    
    private void dumpException(HttpExchange exchange, Exception e) {
        try {
            try {
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(500, 0);
            } catch(IOException e2) { } // maybe already sent
            
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            
            String trace = sw.toString();
            
            trace = trace.replace("\n", "<br>");
            trace = trace.replace("at ", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at ");
            
            PrintWriter pw = new PrintWriter(exchange.getResponseBody());
            
            pw.write(trace);
            
            pw.close();
        } catch(Exception e2) {
            e2.printStackTrace();
        }
    }
}
