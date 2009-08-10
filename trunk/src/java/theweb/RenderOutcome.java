package theweb;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RenderOutcome implements Outcome {

    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Markup markup = page.markup();
            
            response.setContentType("text/html; charset=UTF-8");
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");
            response.addHeader("Etag", "" + Math.random());

            PrintWriter writer = response.getWriter();

            writer.write(markup.render());
            
            writer.flush();
        } finally {
            Messages.get().clear();
        }
    }
}
