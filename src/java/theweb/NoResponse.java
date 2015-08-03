package theweb;

import java.io.IOException;
import java.io.PrintWriter;

public class NoResponse implements Response {
    @Override
    public void send(Page page, HttpExchange exchange) throws IOException {
        // firefox requires empty response to be written!
        exchange.setContentType("text/html");

        PrintWriter writer = exchange.getWriter();
        writer.write("");
    	writer.close();
    }
}
