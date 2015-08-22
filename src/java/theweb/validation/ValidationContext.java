package theweb.validation;

import java.util.*;

/** 
 * Validation context represents set of messages for a particular object.
 * Context is changed on processing @Valid annotation
 * 
 * all getMessages will descend to child contexts
 * all global message methods will delegate to root context
 */
public class ValidationContext {
    private Map<String, Message> messages = new LinkedHashMap<String, Message>();
    private Map<String, ValidationContext> children = new LinkedHashMap<String, ValidationContext>();
    private ValidationContext root;
    
    public ValidationContext() {
        this.root = this;
    }
    
    public ValidationContext(ValidationContext root) {
        this.root = root;
    }
    
    public ValidationContext child(String name) {
        if (children.containsKey(name)) return children.get(name);
        
        ValidationContext child = new ValidationContext(root);

        children.put(name, child);
        
        return child;
    }

    public boolean hasMessages() {
        return !getMessages().isEmpty();
    }
    
    public boolean hasMessage(String field) {
        return getMessages().containsKey(field);
    }
    
    public void addMessage(String field, String key, Object... args) {
        addMessage(field, new Message(key, args));
    }

    public void addMessage(String field, Message message) {
        messages.put(field, message);
    }

    public String message(String field) {
        if (!hasMessage(field)) return "";
        
        return getMessages().get(field).asString();
    }
    
    public Map<String, Message> getMessages() {
        Map<String, Message> m = new HashMap<String, Message>();
        
        m.putAll(this.messages);
        
        for (String name : children.keySet()) {
            Map<String, Message> childMessages = children.get(name).getMessages();
            
            for (String field : childMessages.keySet())
                m.put(name + "." + field, childMessages.get(field));
        }
        
        return m;
    }
    
    public static final String GLOBAL = "global";
    
    public String message() {
        return root.message(GLOBAL);
    }
    
    public void addGlobalMessage(Message message) {
        root.addMessage(GLOBAL, message);
    }

    public void addGlobalMessage(String key, Object... args) {
        addGlobalMessage(new Message(key, args));
    }
    
    public boolean hasGlobalMessage() {
        return root.hasMessage(GLOBAL);
    }
}
