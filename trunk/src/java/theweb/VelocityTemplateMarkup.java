package theweb;

import java.util.HashMap;
import java.util.Map;

import theweb.velocity.VelocityTemplate;

public class VelocityTemplateMarkup implements Markup {
    
    private VelocityTemplate velocityTemplate;
    private Map<String, Object> context = new HashMap<String, Object>();

    public VelocityTemplateMarkup(Class<?> base, String template, Object ... context) {
        this.velocityTemplate = new VelocityTemplate(base, template);
        
        for (int i = 0; i < context.length; i = i + 2)
            this.context.put(context[i].toString(), context[i + 1]);
    }

    @Override
    public String render() {
        return velocityTemplate.render(context);
    }
}
