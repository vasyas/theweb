package theweb.goodies;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** Html builder with JQuery like synt */
public class Html {
    private String tagName;

    public Html(String tagName) {
        this.tagName = tagName;
    }

    private LinkedHashMap<String, String> attributes = new LinkedHashMap<>();
    private List<Object> children = new ArrayList<>();

    public List<Object> children( ) { return children; }
    public Html children(List<Object> children) { this.children = children; return this; }

    public Html title(String s) { return attr("title", s); }
    public Html href(String s) { return attr("href", s); }
    public Html target(String s) { return attr("target", s); }
    public Html clazz(String s) { return attr("class", s); }

    public Html attr(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public Html add(Object o) { children.add(o); return this; }

    public static Html a() { return new Html("a"); }
    public static Html div() { return new Html("div"); }
    public static Html span() { return new Html("span"); }

    public String html() {
        StringBuilder s = new StringBuilder();

        s.append("<");
        s.append(tagName);

        for (String attrName : attributes.keySet()) {
            String attrValue = attributes.get(attrName);

            s.append(" ");
            s.append(attrName);
            s.append("=\"");
            s.append(escape(attrValue));
            s.append("\"");
        }

        if (children.isEmpty() && collapsing())
            s.append("/>");
        else {
            s.append(">");

            for (Object child : children) {
                if (child instanceof Html) s.append(((Html) child).html());
                else s.append(child.toString());
            }

            s.append("</");
            s.append(tagName);
            s.append(">");
        }

        return s.toString();
    }

    private boolean collapsing() {
        return !("span".equals(tagName) || "div".equals(tagName));
    }

    private String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("\'", "&apos;")
                .replace("/", "&#x2F;");
    }
}
