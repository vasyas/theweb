package theweb.goodies;

import theweb.Page;
import theweb.PageState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

/** Html builder */
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

    public Html disabled() { return attr("disabled"); }

    public Html data(String ... params) {
        for (int i = 0; i < params.length; i += 2)
            attr("data-" + params[i], params[i + 1]);

        return this;
    }

    public Html attr(String name) {
        return attr(name, null);
    }

    public Html attr(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    public String getAttribute(String name) {
        return attributes.get(name);
    }

    public Html add(Object o) { children.add(o); return this; }

    public static Html a() { return new Html("a"); }
    public static Html a(Page page, Object content) { return new Html("a").href(PageState.page(page)).add(content); }
    public static Html a(Page page, String method, Object content) { return new Html("a").href(PageState.page(page, method)).add(content); }
    public static Html div() { return new Html("div"); }
    public static Html span() { return new Html("span"); }
    public static Html td() { return new Html("td"); }
    public static Html td(Object child) { return Html.td().add(child); }
    public static Html tr(Object ... children) { Html tr = new Html("tr"); Stream.of(children).forEach(tr::add); return tr; }

    public String html() {
        StringBuilder s = new StringBuilder();

        s.append("<");
        s.append(tagName);

        for (String attrName : attributes.keySet()) {
            String attrValue = attributes.get(attrName);

            s.append(" ");
            s.append(attrName);

            if (attrValue != null) {
                s.append("=\"");
                s.append(escape(attrValue));
                s.append("\"");
            }
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

    public String toString() {
        return html();
    }
}
