package theweb.validation;

import theweb.i18n.Resources;

public class Message {
    public Message(String key, Object... params) {
        this.key = key;
        this.params = params;
    }
    
    public String key;
    public Object[] params;
    
    public String asString() {
        return Resources.getText(key, params);
    }
}
