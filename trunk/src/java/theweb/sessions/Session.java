package theweb.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Session {
    public String id = generate(32);
    
    private static String generate(int length) {
        String s = "";
    
        Random r = new Random();
        
        String validChars = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        
        for (int i = 0; i < length; i++)
            s = s + validChars.charAt((r.nextInt((validChars.length()))));
        
        return s;
    }
    
    
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
    public Object get(String name) { return attributes.get(name); }
    public void set(String name, Object value) { attributes.put(name, value); }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) { return (T) get(type.getName()); }
    public <T> void set(T t) { set(t.getClass().getName(), t);}

    private static ThreadLocal<Session> tl = new ThreadLocal<Session>();
    
    public static void set(Session session) {
        tl.set(session);
    }
    
    public static Session get() {
        return tl.get();
    }

    private long lastAccessTime = System.currentTimeMillis();
    
    void access() {
        lastAccessTime = System.currentTimeMillis();
    }
    
    boolean expired(long sessionTimeout) {
        return System.currentTimeMillis() > lastAccessTime + sessionTimeout;
    }

	public void invalidate() {
		id = ""; // invalidates session, see SessionManager.getOrCreateSession
	}
    
}