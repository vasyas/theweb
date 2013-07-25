package theweb.pagefinder;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.AbstractScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.collect.Multimap;
import theweb.Page;

class PageFinderCache {
    private static Map<String, List<Class<? extends Page>>> map = new HashMap<String, List<Class<? extends Page>>>();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Class<? extends Page>> getPageClasses(String packagePrefix) {
        if (map.containsKey(packagePrefix)) return map.get(packagePrefix);
        
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.getContextClassLoader());
        classLoadersList.add(ClasspathHelper.getStaticClassLoader());
        
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setScanners(new PagesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
            .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packagePrefix))));
        
        Multimap<String, String> s = reflections.getStore().get(PagesScanner.class);
        
        List r = ReflectionUtils.forNames(s.keySet());
        
        map.put(packagePrefix, r);
        
        return r;
    }
    
    private static class PagesScanner extends AbstractScanner {
        private boolean applicable(String className) {
            try {
                Class<?> cls = Class.forName(className);
                
                if (Modifier.isAbstract(cls.getModifiers())) return false;
                
                return Page.class.isAssignableFrom(cls);
            } catch(Exception e) {
                return false;
            }
        }
        
        @SuppressWarnings({"unchecked"})
        public void scan(Object cls) {
            String className = getMetadataAdapter().getClassName(cls);
            
            if (applicable(className) && acceptResult(className))
                getStore().put(className, className);
        }
    }
}
