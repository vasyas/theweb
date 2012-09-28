package theweb;

public class Markups implements Markup {

    private final Markup[] markups;
    private String separator = ""; 

    public Markups(String separator, Markup ... markups) {
    	this.separator = separator;
    	this.markups = markups;
    }
    
    public Markups(Markup ... markups) {
    	this("", markups);
    }
    
    @Override
    public String render() {
        StringBuilder s = new StringBuilder();
        
        for (Markup markup : markups) {
        	if (s.length() > 0)
        		s.append(separator);
        	
            s.append(markup.render());
        }
        
        return s.toString();
    }

}
