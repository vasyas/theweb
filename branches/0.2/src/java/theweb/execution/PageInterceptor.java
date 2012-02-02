package theweb.execution;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageInterceptor {
    Object execute(Execution execution, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
