package theweb;

import theweb.html.StringMarkup;

public class Page {
    protected final PathPattern pathPattern;

    public Page(String baseUrl) {
        this(new PathPattern(baseUrl));
    }
    
    public Page(PathPattern pathPattern) {
        if (!pathPattern.pattern.endsWith("/"))
            throw new IllegalStateException("Page urls should ends with /");
        
        this.pathPattern = pathPattern;
    }
    
    public Markup markup() throws Exception { 
        return new StringMarkup(""); 
    }
}
