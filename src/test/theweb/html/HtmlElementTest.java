package theweb.html;

import junit.framework.TestCase;

public class HtmlElementTest extends TestCase {
    public void testHtml() {
        HtmlElement element = new ElementWithoutContent();

        element.tagName = "e";
        element.set("a", "1");
        element.set("b", null);
        element.set("c", "2");

        assertEquals("<e a=\"1\" b c=\"2\"/>", element.render());

        element = new ElementWithContent();

        element.tagName = "e";
        element.set("a", "1");
        element.set("b", null);
        element.set("c", "2");

        assertEquals("<e a=\"1\" b c=\"2\">1</e>", element.render());
    }
    
    public void testChildren() throws Exception {
        HtmlElement el = new HtmlElement("parent");
        
        el.add(new HtmlElement("child"));
        
        assertEquals("<parent><child/></parent>", el.render());
    }
    
    public void testSetAttributes() {
        HtmlElement el = new HtmlElement();

        el.set("a", "1");
        assertEquals("1", el.get("a"));

        el.set("a", "2");
        assertEquals("2", el.get("a"));

        try {
            el.set(null, "1");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testAttributesToString() {
        HtmlElement el = new HtmlElement("e");
        el.set("a", "1");
        el.set("b", null);
        el.set("c", "3");

        assertEquals("<e a=\"1\" b c=\"3\"/>", el.render());
    }

    public void testEscape() {
        HtmlElement el = new HtmlElement("e");
        el.set("a", "\"");

        assertEquals("<e a=\"&quot;\"/>", el.render());
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

