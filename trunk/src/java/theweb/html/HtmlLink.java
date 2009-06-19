package theweb.html;

public class HtmlLink extends HtmlElement {
    public HtmlLink(String href) {
        super("a");
        
        set("href", href);
    }
    
    public HtmlLink(String href, String content) {
        super("a", content);
        
        set("href", href);
    }
    
    public HtmlLink(String href, HtmlElement content) {
        super("a", content);
        
        set("href", href);
    }

    public HtmlLink setOnclick(String string) {
        set("onclick", string);
        
        return this;
    }
}
