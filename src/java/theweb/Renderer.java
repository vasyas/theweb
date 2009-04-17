package theweb;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

class Renderer {

    public void render(Page page, HttpServletResponse response) throws Exception {
        Markup markup = page.markup();
        
        response.setContentType("text/html; charset=UTF-8");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("Etag", "" + Math.random());

        PrintWriter writer = response.getWriter();

        writer.write(markup.render());
        
        writer.flush();
    }

}