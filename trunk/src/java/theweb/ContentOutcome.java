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

    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(contentType);
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

        PrintWriter writer = response.getWriter();

        Messages messages = Messages.get();
        
        try {
            writer.write(content);
        } finally {
            messages.clear();
        }
        
        writer.flush();
    }

}
