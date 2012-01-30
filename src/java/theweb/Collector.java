package theweb;

import java.util.Map;

interface Collector {
    void collect(Map<String, Object> properties, HttpExchange exchange);
}
