package theweb.html;

import theweb.html.HtmlElement;
import junit.framework.TestCase;

public class HtmlElementTest extends TestCase {
    public void testHtml() {
        HtmlElement element = new ElementWithoutContent();

        element.tagName = "e";
        element.set("a", "1");
        element.setAttribute("b");
        element.set("c", "2");

        assertEquals("<e a=\"1\" b c=\"2\"/>", element.html());

        element = new ElementWithContent();

        element.tagName = "e";
        element.set("a", "1");
        element.setAttribute("b");
        element.set("c", "2");

        assertEquals("<e a=\"1\" b c=\"2\">1</e>", element.html());
    }
    
    public void testChildren() throws Exception {
        HtmlElement el = new HtmlElement("parent");
        
        el.add(new HtmlElement("child"));
        
        assertEquals("<parent><child/></parent>", el.html());
    }
    
    public void testSetAttributes() {
        HtmlElement el = new HtmlElement();

        el.set("a", "1");
        assertEquals("1", el.getAttribute("a"));

        el.set("a", "2");
        assertEquals("2", el.getAttribute("a"));

        try {
            el.set(null, "1");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testAttributesToString() {
        HtmlElement el = new HtmlElement("e");
        el.set("a", "1");
        el.setAttribute("b");
        el.set("c", "3");

        assertEquals("<e a=\"1\" b c=\"3\"/>", el.html());
    }

    class ElementWithContent extends HtmlElement {
        @Override
        protected String getContent() {
            return "1";
        }
    }

    class ElementWithoutContent extends HtmlElement {
        @Override
        protected String getContent() {
            return null;
        }
    }    
}

