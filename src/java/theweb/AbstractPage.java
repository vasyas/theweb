package theweb;

import theweb.html.StringMarkup;

public class AbstractPage implements Page {
    private final PathPattern pathPattern;

    public AbstractPage(String baseUrl) {
        this(new PathPattern(baseUrl));
    }
    
    public AbstractPage(PathPattern pathPattern) {
        if (!pathPattern.pattern.endsWith("/"))
            throw new IllegalStateException("Page urls should ends with /");
        
        this.pathPattern = pathPattern;
    }
    
    public Markup markup() throws Exception { 
        return new StringMarkup(""); 
    }
    
    @Override
    public PathPattern getPathPattern() {
        return pathPattern;
    }
}
