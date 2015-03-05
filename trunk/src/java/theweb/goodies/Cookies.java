package theweb.goodies;

import com.sun.net.httpserver.HttpExchange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cookies {
    private final HttpExchange exchange;

    public Cookies(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public String get(String name) {
        String cookie = exchange.getRequestHeaders().getFirst("Cookie");

        if (cookie == null) return null;

        Matcher matcher = Pattern.compile(".*" + name + "=(\\w+).*").matcher(cookie);

        if (!matcher.matches()) return null;

        return matcher.group(1);
    }

    private final String expires_future = "Tue, 3 Jul 2040 21:47:38 GMT";
    private final String expires_past = "Tue, 15 Jan 2013 21:47:38 GMT";

    public void add(String name, String value) {
        exchange.getResponseHeaders().add("Set-Cookie", name + "=" + value + "; path=/; expires=" + expires_future);
    }

    public void remove(String name) {
        exchange.getResponseHeaders().add("Set-Cookie", name + "=empty; path=/; expires=" + expires_past);
    }
}
