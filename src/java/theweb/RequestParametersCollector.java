package theweb;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class RequestParametersCollector implements Collector {
    @SuppressWarnings("unchecked")
    public void collect(Map<String, Object> properties, HttpServletRequest request) {
        // trim all Strings
        for (Map.Entry entry : (Set<Map.Entry>)request.getParameterMap().entrySet()) {
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
