package theweb.velocity;

import org.apache.log4j.Logger;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class VelocityLogChute implements LogChute {
    private final Logger log = Logger.getLogger(VelocityLogChute.class);
    
    public void init(RuntimeServices rs) throws Exception {}

    public void log(int level, String s) {
        log(level, s, null);
    }

    public void log(int level, String s, Throwable throwable) {
        if (s.contains("reference"))
            log.warn(s, throwable);
    }

    public boolean isLevelEnabled(int level) {
        return level == 0;
    }
}