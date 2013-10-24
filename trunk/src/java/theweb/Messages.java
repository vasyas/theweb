package theweb;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import theweb.i18n.Resources;

public class Messages {
    private LinkedHashSet<String> errors = new LinkedHashSet<String>();
    private LinkedHashSet<String> results = new LinkedHashSet<String>();

    public List<String> getErrors() {
    	return new ArrayList<String>(errors);
    }
    
    public List<String> getResults() {
    	return new ArrayList<String>(results);
    }
    
	public void clear() {
		results.clear();
		errors.clear();
	}

    private static ThreadLocal<Messages> messages = new ThreadLocal<Messages>();
    
	public static void result(String s) {
		messages.get().results.add(Resources.getText(s));
	}
	
	public static void result(String msg, Object ... args) {
		messages.get().results.add(Resources.getText(msg, args));
	}
	 
	public static void error(String s) {
		messages.get().errors.add(Resources.getText(s));
	}
	
	public static void error(String s, Object ... args) {
		messages.get().errors.add(Resources.getText(s, args));
	}
	
	public static boolean hasErrors() {
		return !messages.get().errors.isEmpty();
	}

	public static boolean hasResults() {
		return !messages.get().results.isEmpty();
	}
               
	public void set() {
		messages.set(this);
	}

    public static Messages get() {
        return messages.get();
    }
}
