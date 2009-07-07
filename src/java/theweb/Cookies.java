package theweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public Cookies(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public Cookie getCookie(String name) {
        if (request.getCookies() == null) return null; // for httpclient
        
        for (Cookie cookie : request.getCookies())
            if ( cookie.getName().equals(name) ) return cookie;
        return null;
    }
    
    public List<Cookie> getCookies() {
        return new ArrayList<Cookie>(Arrays.asList(request.getCookies()));
    }

    public void addCookie(Cookie cookie) {
        response.addCookie(cookie);
    }
    
    public void addCookie(String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        
        addCookie(cookie);
    }

    public void addCookie(String name, String value) {
        addCookie(name, value, 60 * 60 * 24 * 365, "/");
    }

    public String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        
        if (cookie == null) return null;
        
        return cookie.getValue();
    }
}
