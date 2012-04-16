package theweb.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import theweb.Markup;

public class HtmlElement implements Markup {

    public String tagName;

    private Map<String, String> attributes = new LinkedHashMap<String, String>();
    private List<Markup> children = new ArrayList<Markup>();
    
    public HtmlElement() {
    }
    
    public HtmlElement(String tagName) {
        this.tagName = tagName;
    }
    
    public HtmlElement(String tagName, String content) {
        this.tagName = tagName;
        
        if (content != null)
            add(new StringMarkup(content));
    }
    
    public HtmlElement(String tagName, Markup content) {
        this.tagName = tagName;
        
        add(content);
    }
    
    @Override
    public String render() {
        String attributes = attributesToString();
        if ( attributes.length() > 0 ) attributes = " " + attributes;

        String html = "<" + tagName + attributes;

        String content = getContent();
        
        if ( content == null )
            html += "/>";
        else {
            html += ">";
            html += content;
            html += "</" + tagName + ">";
        }

        return html;
    }

    protected String getContent() {
        if (children.isEmpty()) return null;
        
        StringBuilder s = new StringBuilder();

        for (Markup child : children)
            s.append(child.render());
        
        return s.toString();
    }

    public String get(String name) {
        return attributes.get(name);
    }

    public HtmlElement set(String name) {
        return set(name, null);
    }
    
    public HtmlElement set(String name, String value) {
        if ( name == null ) throw new IllegalArgumentException("name is null");

        attributes.put(name, value);
        
        return this;
    }

    private String attributesToString() {
        String string = "";

        for (String name : attributes.keySet()) {
            if ( string.length() > 0 ) string += " ";

            String value = attributes.get(name);

            if ( value != null )
                string += name + "=\"" + get(name) + "\"";
            else
                string += name;
        }

        return string;
    }

    public HtmlElement add(Markup child) {
        children.add(child);
        return this;
    }
    
    public HtmlElement inject(HtmlElement parent) {
        parent.add(this);
        return this;
    }

    public HtmlElement setClass(String value) {
        return set("class", value);
    }
    
    public HtmlElement setId(String value) {
        return set("id", value);
    }
    
    public HtmlElement setStyle(String value) {
        return set("style", value);
    }

    public List<Markup> getChildren() {
        return children;
    }
    
    public HtmlElement clone() {
        HtmlElement element = new HtmlElement(tagName);
        
        element.attributes = new HashMap<String, String>(attributes);
        element.children = new ArrayList<Markup>(children);
        
        return element;
    }
}
