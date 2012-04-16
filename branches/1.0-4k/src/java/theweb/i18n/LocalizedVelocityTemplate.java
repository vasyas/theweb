package theweb.i18n;

import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import theweb.velocity.VelocityTemplate;

public class LocalizedVelocityTemplate extends VelocityTemplate {
    
    public LocalizedVelocityTemplate(String path) {
        super(path);
    }

    public LocalizedVelocityTemplate(Class<?> clazz, String localPath) {
        super(clazz, localPath);
    }
    
    @Override
    protected Template getTemplate() throws ResourceNotFoundException, ParseErrorException, Exception {
        int idx = path.lastIndexOf('.');
        
        if (idx == -1)
            idx = path.length() - 1;
        
        String base = path.substring(0, idx);
        String ext = "";
        
        if (idx < path.length() - 1) ext = path.substring(idx);
        
        // try locale-specific
        String p = base + "_" + Resources.getLocale() + ext;
        
        try {
            return ve.getTemplate(p, "UTF-8");
        } catch(ResourceNotFoundException e) {
            // defaults to generic
            
            return ve.getTemplate(path, "UTF-8");
        }
    }
}
