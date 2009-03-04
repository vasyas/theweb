package theweb.velocity;

import org.apache.log4j.Logger;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

public class VelocityLogSystem implements LogSystem {
    private final Logger log = Logger.getLogger(VelocityLogSystem.class);
    
    public void init(RuntimeServices rs) throws Exception {}

    public void logVelocityMessage(int level, String message) {
        // limit messages
        if (level == LogSystem.WARN_ID || level == LogSystem.ERROR_ID) {
            if (message.contains("is an Iterator")) return;
            if (message.contains("unable to find resource")) return;
            
            log.warn("[velocity] " + message);
        }
    }
    
}