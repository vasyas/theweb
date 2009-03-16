package theweb;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface Collector {

    void collect(Map<String, Object> properties, HttpServletRequest request);

}
