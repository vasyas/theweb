package theweb.execution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.Outcome;

public interface PageInterceptor {
    Outcome execute(Execution execution, HttpServletRequest request, HttpServletResponse response);
}
