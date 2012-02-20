package theweb;

import java.util.Map;

public interface Collector {
    void collect(Map<String, Object> properties, HttpExchange exchange);
}
