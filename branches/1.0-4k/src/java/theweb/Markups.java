package theweb;

public class Markups implements Markup {

    private final Markup[] markups;

    public Markups(Markup ... markups) {
        this.markups = markups;
    }
    
    @Override
    public String render() {
        StringBuilder s = new StringBuilder();
        
        for (Markup markup : markups)
            s.append(markup.render());
        
        return s.toString();
    }

}
