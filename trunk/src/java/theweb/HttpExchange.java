package theweb;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Map;

public interface HttpExchange {
    String getContextPath();
    
    String getRequestPath();
    String getRequestQuery();
    String getRequestMethod();
    String getRequestHeader(String name);
    Map<String, Object> getRequestParameters();
    InputStream getInputStream();
    InetSocketAddress getRemoteAddr();

    void setContentType(String contentType);
    void setContentLength(long length);

    PrintWriter getWriter();
    OutputStream getOutputStream();

    void sendError(int error, String msg);
    void sendError(int error);
    void sendRedirect(String to);
    void addResponseHeader(String name, String value);
}
