package theweb.html;

import theweb.Markup;
import theweb.Page;
import theweb.PageState;

public class HtmlLink extends HtmlElement {
    public HtmlLink(String href) {
        this(href, new StringMarkup(""));
    }
    
    public HtmlLink(String href, String content) {
        this(href, new StringMarkup(content));
    }
    
    public HtmlLink(Page page, Markup content) {
        this(new PageState(page).view(), content);
    }
    
    public HtmlLink(Page page, String content) {
        this(new PageState(page).view(), content);
    }
    
    public HtmlLink(String href, Markup content) {
        super("a");
        
        set("href", href);
        
        add(content);
    }
    
    public HtmlLink setOnclick(String string) {
        set("onclick", string);
        
        return this;
    }
    
    @Override
    public HtmlLink set(String name, String value) {
        super.set(name, value);
        
        return this;
    }
}
