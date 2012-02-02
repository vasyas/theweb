package theweb.resources;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class ClasspathResourceLocation implements ResourceLocation {
	private final String name;

	public ClasspathResourceLocation(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    
	    InputStream resourceStream = loader.getResourceAsStream(name);
	    
	    return resourceStream;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String getPath() {
	    return getName();
	}

	public long lastModified() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        
        URL resource = loader.getResource(name);
        
        if (resource == null)
        	return 0;
        
        if ("file".equals(resource.getProtocol())) {
            try {
                java.io.File file = new java.io.File(resource.toURI());
                
                return file.lastModified();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
        	return 0; // no reload
        }
	}
	
	@Override
	public String toString() {
		return "CP:" + name;
	}
}