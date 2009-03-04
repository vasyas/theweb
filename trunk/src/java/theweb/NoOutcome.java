package theweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoOutcome implements Outcome {

    @Override
    public void process(Page page, HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

}
