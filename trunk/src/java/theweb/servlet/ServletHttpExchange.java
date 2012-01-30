package theweb.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.HttpExchange;

public class ServletHttpExchange implements HttpExchange {
    
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ServletHttpExchange(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    

    @Override
    public String getContextPath() {
        return request.getContextPath();
    }

    @Override
    public String getRequestPath() {
        if (request.getPathInfo() == null)
            return request.getServletPath();
        else
            return request.getPathInfo();
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getParameters() {
        return request.getParameterMap();
    }

    @Override
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    @Override
    public void setContentLength(long length) {
        response.setContentLength((int) length);
    }

    @Override
    public PrintWriter getWriter() {
        try {
            return response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            return response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendError(int error) {
        try {
            response.sendError(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendRedirect(String to) {
        try {
            response.sendRedirect(to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        response.addHeader(name, value);
    }

}
