package theweb;

import java.util.Map;

public interface Populator {

    void populate(Page page, Map<String, Object> properties);

}
