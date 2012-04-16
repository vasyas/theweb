package theweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Outcome {
    void process(Page page, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
