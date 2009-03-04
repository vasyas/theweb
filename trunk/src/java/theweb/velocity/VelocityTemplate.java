package theweb.velocity;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityTemplate {
    private static List<Class<? extends Directive>> directives = new ArrayList<Class<? extends Directive>>();

    protected static VelocityEngine ve;
    static {
        initializeVelocityEngine();
    }

    private static void initializeVelocityEngine() {
        ve = new VelocityEngine();

        ve.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
        
        ve.setProperty(RuntimeConstants.UBERSPECT_CLASSNAME, FieldAwareUberspect.class.getName());
        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, VelocityLogSystem.class.getName());

        // disable cache
        ve.setProperty(RuntimeConstants.VM_LIBRARY_AUTORELOAD, "true");
        ve.setProperty("clspth.resource.loader.cache", "false");
        ve.setProperty("velocimacro.permissions.allow.inline.to.replace.global", "true");

        // webapp loader
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "clspth");
        ve.setProperty("clspth.resource.loader.class", ClasspathResourceLoader.class.getName());

        // directives
        for (Class<? extends Directive> directiveClass : directives)
            ve.addProperty("userdirective", directiveClass.getName());

        try {
            ve.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addDirective(Class<? extends Directive> directiveClass) {
        directives.add(directiveClass);
        initializeVelocityEngine();
    }

    public static String evaluate(String template, Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        try {
            VelocityTemplate.ve.evaluate(new VelocityContext(context), writer, "expression", template);
            return writer.getBuffer().toString();
        } catch (MethodInvocationException e) {
            if ( e.getCause() == null )
                e.initCause(e.getWrappedThrowable());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String path;

    public VelocityTemplate(Class<?> clazz, String localPath) {
        this.path = clazz.getPackage().getName().replace('.', '/') + "/" + localPath;
    }
    
    public VelocityTemplate(String path) {
        this.path = path;
    }

    public void render(Writer writer, Map<String, Object> context) {
        try {
            Template velocityTemplate = getTemplate();
            velocityTemplate.merge(new VelocityContext(context), writer);
        } catch (MethodInvocationException e) {
            if ( e.getCause() == null )
                e.initCause(e.getWrappedThrowable());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Template getTemplate() throws ResourceNotFoundException, ParseErrorException, Exception {
        return ve.getTemplate(path, "UTF-8");
    }

    public String render(Map<String, Object> context) {
        StringWriter stringWriter = new StringWriter();
        render(stringWriter, context);
        return stringWriter.getBuffer().toString();
    }
}
