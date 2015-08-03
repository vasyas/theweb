package theweb.goodies;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static theweb.goodies.Html.*;

public class HtmlTest {
    @Test
    public void empty() {
        asrt("<a/>", a());
        asrt("<a></a>", a().add(""));
    }

    @Test
    public void attrs() {
        asrt("<a href=\"#\"/>", a().href("#"));
        asrt("<a q/>", a().attr("q"));
    }

    @Test
    public void specialNonCollapsing() {
        asrt("<a/>", a());
        asrt("<span></span>", span());
        asrt("<div></div>", div());
    }

    @Test
    public void donotEscapeContent() {
        asrt("<div><b>1</b></div>", div().add("<b>1</b>"));
    }

    @Test
    public void escapeAttrs() {
        asrt("<div class=\"&lt;b&gt;\"></div>", div().clazz("<b>"));
    }

    private void asrt(String expected, Html html) {
        assertEquals(expected, html.html());
    }
}