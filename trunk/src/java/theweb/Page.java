package theweb;

public abstract class Page {
    protected final String baseUrl;

    public Page(String baseUrl) {
        if (!baseUrl.endsWith("/"))
            throw new IllegalStateException("Page urls should ends with /");
        
        this.baseUrl = baseUrl;
    }
    
    public abstract Markup markup();
}
