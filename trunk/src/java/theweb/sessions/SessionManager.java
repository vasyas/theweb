package theweb.sessions;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class SessionManager {
    private final long sessionTimeout;
    private Consumer<Session> initSession = (s) -> {};

    public SessionManager(long scavengePeriod, long sessionTimeout, ConcurrentHashMap<String, Session> sessions) {
        this.sessionTimeout = sessionTimeout;
        this.sessions = sessions;
        
        new Timer("SessionScavenger", true).schedule(new TimerTask() {
            @Override
            public void run() {
                scavenge();
            }
        }, 0, scavengePeriod);
    }

    private ConcurrentMap<String, Session> sessions;

    /**
     * If id is null, create new session
     */
    public Session getOrCreateSession(String id) {
        if (id == null) return newSession();
        
        Session session = sessions.get(id);
        
        if (session == null || !id.equals(session.id)) return newSession();
        
        session.access();
           
        return session;
    }

    private Session newSession() {
        Session session = new Session();
        
        sessions.put(session.id, session);

        initSession.accept(session);
        
        return session;
    }
    
    void remove(String sessionId) {
    	sessions.remove(sessionId);
    }
    
    public void scavenge() {
        for (Iterator<Session> i = sessions.values().iterator(); i.hasNext(); ) {
            Session session = i.next();
            
            if (session.expired(sessionTimeout))
                i.remove();
        }
    }

    public int getSessions() {
        return sessions.size();
    }

    public SessionManager initSession(Consumer<Session> initSession) {
        this.initSession = initSession;
        return this;
    }
}
