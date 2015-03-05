package theweb.resources;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceScanner {
	private final ResourceListener listener;

	public ResourceScanner(ResourceListener listener) {
		this.listener = listener;
	}
	
    public boolean findInFileSystem(File root) {
    	// no such folder - can't continue
    	if (!root.exists())
    		return false;
    	
        for (File file : root.listFiles()) {
            if (file.isFile())
                listener.resourceFound(new FileResourceLocation(file));
            
            if (file.isDirectory())
                findInFileSystem(file);
        }
        
        return true;
    }

    public static String getJarPath(String jarName) {
        String classpath = System.getProperty("java.class.path");

        StringTokenizer tokenizer = new StringTokenizer(classpath, java.io.File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();

            if (path.contains(jarName))
                return path;
        }

        return null;
    }

    public boolean findInClasspath(String jarName) {
        String jarPath = getJarPath(jarName);
        
        // no jar in classpath - can't continue
        if (jarPath == null)
            return false;
        
        try {
		    JarFile jarFile = new JarFile(jarPath);
		
		    for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
		        JarEntry entry = entries.nextElement();
		        
		        if (!entry.isDirectory()) {
		        	listener.resourceFound(new ClasspathResourceLocation(entry.getName()));
		        }
		    }
		    
		    jarFile.close();
		} catch(IOException e) {
		    throw new RuntimeException(e);
		}
        
        return true;
    }
}
