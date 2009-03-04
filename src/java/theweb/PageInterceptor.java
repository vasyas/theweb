package theweb;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageInterceptor {
    public boolean beforePopulate(Page page, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
