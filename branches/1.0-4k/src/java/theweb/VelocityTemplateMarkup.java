package theweb;

import java.util.HashMap;
import java.util.Map;

import theweb.velocity.VelocityTemplate;

public class VelocityTemplateMarkup implements Markup {
    
    private VelocityTemplate velocityTemplate;
    public Map<String, Object> context;

    public VelocityTemplateMarkup(Class<?> base, String template, Object ... context) {
        this(base, template, toMap(context));
    }
    
    private static Map<String, Object> toMap(Object[] arr) {
        Map<String, Object> context = new HashMap<String, Object>();
        
        for (int i = 0; i < arr.length; i = i + 2)
            context.put(arr[i].toString(), arr[i + 1]);
        
        return context;
    }

    public VelocityTemplateMarkup(Class<?> base, String template, Map<String, Object> context) {
        this.velocityTemplate = new VelocityTemplate(base, template);
        this.context = context;
    }

    @Override
    public String render() {
        return velocityTemplate.render(context);
    }
}
