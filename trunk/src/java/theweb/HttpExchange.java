package theweb;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

public interface HttpExchange {
    String getContextPath();
    String getRequestPath();
    String getHeader(String name);
    Map<String, Object> getParameters();

    void setContentType(String contentType);
    void setContentLength(long length);

    PrintWriter getWriter();
    OutputStream getOutputStream();

    void sendError(int error);
    void sendRedirect(String to);
    void addHeader(String name, String value);
}
