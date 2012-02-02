package theweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(contentType);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("Etag", "" + Math.random());

        PrintWriter writer = response.getWriter();

        writer.write(content);
        
        writer.flush();
    }

}
