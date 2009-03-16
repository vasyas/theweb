package theweb;

import theweb.html.StringMarkup;

public class Page {
    protected final String baseUrl;

    public Page(String baseUrl) {
        if (!baseUrl.endsWith("/"))
            throw new IllegalStateException("Page urls should ends with /");
        
        this.baseUrl = baseUrl;
    }
    
    public Markup markup() throws Exception { 
        return new StringMarkup(""); 
    }
}
