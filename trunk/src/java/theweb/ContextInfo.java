package theweb;

public class ContextInfo {
    public final String contextPath;
    
    ContextInfo(String contextPath) {
        this.contextPath = contextPath;
        
        tl.set(this);
    }
    
    public static ContextInfo getCurrent() {
        return tl.get();
    }

    static void clear() {
        tl.set(null);
    }
    
    private final static ThreadLocal<ContextInfo> tl = new ThreadLocal<ContextInfo>();
}
