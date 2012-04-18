package theweb;

import theweb.html.StringMarkup;

public class AbstractPage implements Page {
    private final PathPattern pathPattern;

    public AbstractPage(String baseUrl) {
        this(new PathPattern(baseUrl));
    }
    
    public AbstractPage(PathPattern pathPattern) {
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
