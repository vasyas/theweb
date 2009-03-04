package theweb.html;

public class HtmlLink extends HtmlElement {
    public HtmlLink(String href) {
        super("a");
        
        set("href", href);
    }

    public HtmlLink setOnclick(String string) {
        set("onclick", string);
        
        return this;
    }
}
