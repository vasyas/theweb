package theweb.html;

public class HtmlImg extends HtmlElement {
    public HtmlImg(String src) {
        super("img");
        
        set("src", src);
    }
}
