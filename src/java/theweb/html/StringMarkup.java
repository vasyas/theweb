package theweb.html;

import theweb.Markup;

public class StringMarkup implements Markup {

    private final String s;

    public StringMarkup(String s) {
        this.s = s;
    }
    
    @Override
    public String render() {
        return s;
    }

}
