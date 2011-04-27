package theweb.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import theweb.resources.ResourceLocation;

public class Resources {
	public static void addBundle(ResourceLocation location) {  
        int begin = location.getName().lastIndexOf('_');
        
        if (begin == -1)
            throw new IllegalArgumentException("Resource name must ends with '_<lang>.properties' and it is '" + location + "'");
        
        int end = location.getName().indexOf('.', begin);
        
        if (end == -1)
            throw new IllegalArgumentException("Resource name must ends with '_<lang>.properties' and it is '" + location + "'");
        
        String locale = location.getName().substring(begin + 1, end);
        
        Bundles bundles = localeBundles.get(locale);
        
        if (bundles == null) {
            bundles = new Bundles();
            localeBundles.put(locale, bundles);
        }
        
        Bundle bundle = new Bundle(location);
        
        bundles.addBundle(bundle);
	}
	
	public static boolean hasText(String key) {
	    if (currentLocale.get() == null)
	        throw new IllegalStateException("No locale set");
	    
	    String currentLocaleText = getTextInLocale(key, currentLocale.get());
	    
	    if (currentLocaleText != null) return true;
	    
	    if (defaultLocale != null) {
	        String defaultLocaleText = getTextInLocale(key, defaultLocale);
	        
	        if (defaultLocaleText != null) return true;
	    }
	    
	    return false;
	}
	
	public static String i18n(String key) {
	    return getText(key);
	}
	
	public static String i18n(String key, Object ... args) {
	    return getText(key, args);
	}
	
	public static String i(String key) {
	    return getText(key);
	}
	
	public static String i(String key, Object ... args) {
	    return getText(key, args);
	}
	
    public static String getText(String key) {
        if (currentLocale.get() == null)
            throw new IllegalStateException("No locale set");
        
        String currentLocaleText = getTextInLocale(key, currentLocale.get());
        
        if (currentLocaleText != null) return currentLocaleText;
        
        if (defaultLocale != null) {
			String defaultLocaleText = getTextInLocale(key, defaultLocale);

			if (defaultLocaleText != null) return defaultLocaleText;
		}
        
        return key;
    }
    
    private static Pattern referencesPattern = Pattern.compile("#\\{(.+?)\\}");
    
    private static String getTextInLocale(String key, String locale) {
        return getTextInLocaleRecursiveSafe(new HashSet<String>(), key, locale);
    }
    
    private static String getTextInLocaleRecursiveSafe(Set<String> visitedKeys, String key, String locale) {
        if (visitedKeys.contains(key)) return key;
        visitedKeys.add(key);
        
        // check thread-local properties first
        Map<String, String> threadLocalProperties = threadLocalBundle.get();
        
        if (threadLocalProperties != null) {
            String value = threadLocalProperties.get(key);
            
            if (value != null) return value;
        }
        
        // regular bundles
        Bundles bundles = localeBundles.get(locale);
        
        if (bundles == null) return null;
        
        String value = bundles.getProperty(key);
        
        if (value == null) return null;
        
        StringBuffer r = new StringBuffer();
        
        Matcher m = referencesPattern.matcher(value);
        
        while (m.find()) {
            String referencedKey = m.group(1);
            String referencedValue = getTextInLocaleRecursiveSafe(visitedKeys, referencedKey, locale);
            
            if (referencedValue == null) referencedValue = referencedKey;
            
            m.appendReplacement(r, Matcher.quoteReplacement(referencedValue));
        }
        
        m.appendTail(r);
        
        return r.toString();
    }

    public static String getText(String key, Object... args) {
        String pattern = getText(key);
        
        pattern = pattern.replaceAll("'", "''");
        
        if (pattern == null)
            return null;

        return MessageFormat.format(pattern, args);
    }
    
    public static void setLocale(String locale) {
        currentLocale.set(locale);
    }

    public static String getLocale() {
        return currentLocale.get();
    }
    
    static ThreadLocal<String> currentLocale = new ThreadLocal<String>();
    static Map<String, Bundles> localeBundles = new HashMap<String, Bundles>();
	
    private static String defaultLocale;
    
	public static void setDefaultLocale(String locale) {
		Resources.defaultLocale = locale;
	}

	static ThreadLocal<Map<String, String>> threadLocalBundle = new ThreadLocal<Map<String, String>>();
	
	/**
	 * Please note, that thread local properties cannot contain references to other properties, 
	 * like regular ones
	 * 
	 * @param key
	 * @param value
	 */
    public static void addThreadLocalProperty(String key, String value) {
        Map<String, String> properties = threadLocalBundle.get();
        
        if (properties == null) {
            properties = new HashMap<String, String>();
            
            threadLocalBundle.set(properties);
        }
        
        properties.put(key, value);
    }

    public static void clearThreadLocalProperties() {
        threadLocalBundle.set(null);
    }
}
