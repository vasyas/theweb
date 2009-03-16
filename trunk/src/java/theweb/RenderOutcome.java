package theweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RenderOutcome implements Outcome {

    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            new Renderer().render(page, response);
        } finally {
            Messages.get().clear();
        }
    }

}
