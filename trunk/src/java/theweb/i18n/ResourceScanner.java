package theweb.i18n;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletContext;

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
                listener.bundleFound(new FileResourceLocation(file));
            
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
		        	listener.bundleFound(new ClasspathResourceLocation(entry.getName()));
		        }
		    }
		    
		    jarFile.close();
		} catch(IOException e) {
		    throw new RuntimeException(e);
		}
        
        return true;
    }

	public boolean findInExplodedWar(ServletContext context) {
        String webPath = context.getRealPath("/");
        
        // web app not exploded - can't continue
        if (webPath == null)
            return false;
        
        return findInFileSystem(new File(webPath));
    }

	public boolean findInTomcatWar(ServletContext servletContext) {
		try {
			Field contextField = servletContext.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			Object context = contextField.get(servletContext);
			
			Field basePathField = context.getClass().getDeclaredField("basePath");
			basePathField.setAccessible(true);
			
			String basePath = (String) basePathField.get(context);
			
			if (basePath == null) return false;
			if (!basePath.endsWith(".war")) return false;
			
			try {
			    JarFile jarFile = new JarFile(basePath);
			
			    for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
			        JarEntry entry = entries.nextElement();
			        
			        if (!entry.isDirectory()) {
			        	listener.bundleFound(new ServletContextResourceLocation(servletContext, entry.getName()));
			        }
			    }
			    
			    jarFile.close();
			} catch(IOException e) {
			    throw new RuntimeException(e);
			}
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchFieldException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		
		return true;
	}
}
