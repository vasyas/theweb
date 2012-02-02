package theweb.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import theweb.resources.ResourceLocation;

class Bundle {
    private final ResourceLocation location;
    
    public Bundle(ResourceLocation location) {
        this.location = location;
        
        load();
    }
    
    private final Map<String, String> properties = new HashMap<String, String>();
    
    private void load() {
        InputStream resourceStream = location.getInputStream();
        
        if (resourceStream == null)
            throw new RuntimeException("Can't find resource '" + location + "'");
        
        try {
        	UnicodeReader streamReader = new UnicodeReader(resourceStream, "UTF-8");
            BufferedReader reader = new BufferedReader(streamReader);
            
            String s;
            
            int lineNumber = 0;
            
            String continueProperty = null;
            
            while ((s = reader.readLine()) != null) {
                s = s.trim();
                
                if (s != null && !"".equals(s) && !s.startsWith("#"))
                    continueProperty = parseLine(++ lineNumber, s, continueProperty);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseLine(int lineNumber, String line, String continueProperty) {
        String key;
        String value;
        
        if (continueProperty == null) {
            int pos = line.indexOf('=');
            
            if (pos == -1)
                return null;
            
            key = line.substring(0, pos).trim();
            value = line.substring(pos + 1).trim();
        } else {
            key = continueProperty;
            value = properties.get(key) + line.trim();
        }
        
        continueProperty = null;
        
        if (value.endsWith("\\")) {
            value = value.substring(0, value.lastIndexOf('\\'));
            continueProperty = key;
        }
        
        properties.put(key, value);
        
        return continueProperty;
    }

    public String getProperty(String key) {
        if (isModified()) {
            properties.clear();

            // catch not found on reloading
            try {
            	load();
            } catch(RuntimeException e) {
            }
        }

        return properties.get(key);
    }

    private long lastModified = -1;

    private boolean isModified() {
        if (lastModified == -1) { 
            lastModified = location.lastModified();
        } else {
            if (lastModified != location.lastModified()) {
                lastModified = location.lastModified();
                
                return true;
            }
        }
        
        return false;
    }

    public String toString() {
        return "Bundle{" + location + "}";
    }
}
