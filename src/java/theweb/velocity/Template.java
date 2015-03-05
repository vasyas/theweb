package theweb.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import theweb.Page;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Template {
    protected String path;
    private Map<String, Object> context = new HashMap<>();

    public static Consumer<Map<String, Object>> defaultContext = (ctx) -> {};

    public Template(Class<?> clazz, String localPath) {
        this(clazz.getPackage().getName().replace('.', '/') + "/" + localPath);
    }

    public Template(String path) {
        this.path = path;

        defaultContext.accept(context);
    }

    public Template(Page page, String path) {
        this(page.getClass(), path);

        context("page", page);
    }

    public Template context(Object... args) {
        for (int i = 0; i < args.length; i = i + 2)
            context.put(args[i].toString(), args[i + 1]);

        return this;
    }

    public Template context(Map<String, Object> context) {
        this.context.putAll(context);
        return this;
    }

    public Map<String, Object> context() {
        return context;
    }

    public String render() {
        StringWriter stringWriter = new StringWriter();
        render(stringWriter);
        return stringWriter.getBuffer().toString();
    }

    public void render(Writer writer) {
        try {
            org.apache.velocity.Template template = ve.getTemplate(path, "UTF-8");
            template.merge(new VelocityContext(context), writer);
        } catch (MethodInvocationException e) {
            if ( e.getCause() == null )
                e.initCause(e.getWrappedThrowable());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return render();
    }

    private static List<Class<? extends Directive>> directives = new ArrayList<Class<? extends Directive>>();

    protected static VelocityEngine ve;
    static {
        initializeVelocityEngine();
    }

    private static void initializeVelocityEngine() {
        ve = new VelocityEngine();

        ve.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");

        ve.setProperty(RuntimeConstants.UBERSPECT_CLASSNAME, FieldAwareUberspect.class.getName());
        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, VelocityLogChute.class.getName());

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
}
