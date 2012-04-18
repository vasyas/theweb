package theweb;

import java.io.PrintWriter;

public class RenderOutcome implements Outcome {

    @Override
    public void process(Page page, HttpExchange exchange) throws Exception {
        try {
            Markup markup = page.markup();
            
            exchange.setContentType("text/html; charset=UTF-8");
            exchange.addResponseHeader("Pragma", "no-cache");
            exchange.addResponseHeader("Expires", "0");
            exchange.addResponseHeader("Etag", "" + Math.random());

            PrintWriter writer = exchange.getWriter();

            writer.write(markup.render());
            
            writer.flush();
        } finally {
            Messages.get().clear();
        }
    }
}
