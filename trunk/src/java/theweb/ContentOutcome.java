package theweb;

import java.io.IOException;
import java.io.PrintWriter;

public class ContentOutcome implements Outcome {
    private final String content;
    private final String contentType;

    public ContentOutcome(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }
    
    public ContentOutcome(String content) {
        this(content, "text/html; charset=UTF-8");
    }

    @Override
    public void process(Page page, HttpExchange exchange) throws IOException {
        exchange.setContentType(contentType);
        exchange.addResponseHeader("Pragma", "no-cache");
        exchange.addResponseHeader("Expires", "0");
        exchange.addResponseHeader("Etag", "" + Math.random());

        PrintWriter writer = exchange.getWriter();

        writer.write(content);
        
        writer.flush();
    }

}
