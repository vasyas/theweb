package theweb;

import java.util.Map;
import java.util.Set;

public class RequestParametersCollector implements Collector {
    @SuppressWarnings("unchecked")
    public void collect(Map<String, Object> properties, HttpExchange exchange) {
        // trim all Strings
        for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) exchange.getParameters().entrySet()) {
            String name = (String) entry.getKey();
            Object value = entry.getValue();

            if ( value instanceof String )
                properties.put(name, ((String)value).trim());
            else if ( value instanceof String[]) {
                String[] strings = (String[]) entry.getValue();

                for (int i = 0; i < strings.length; i ++)
                    strings[i] = strings[i].trim();

                properties.put(name, strings);
            } else
                properties.put(name, value);
        }
    }
}
