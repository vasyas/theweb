package theweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

class Renderer {

    public void render(Page page, HttpServletResponse response) throws IOException {
        Markup markup = page.markup();
        
        response.setContentType("text/html; charset=UTF-8");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

        PrintWriter writer = response.getWriter();

        Messages messages = Messages.get();
        
        try {
            writer.write(markup.html());
        } finally {
            messages.clear();
        }
        
        writer.flush();
    }

}
