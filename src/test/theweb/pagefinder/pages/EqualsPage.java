package theweb.pagefinder.pages;

import theweb.AbstractPage;

public class EqualsPage extends AbstractPage {
    public EqualsPage(String baseUrl) {
        super(baseUrl);
    }
    
    @Override
    public int hashCode() {
        return getPathPattern().pattern.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        
        if (getClass() != obj.getClass())
            return false;
        
        return true;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
