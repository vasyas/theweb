package theweb.pagefinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.apache.log4j.Logger;
import theweb.Page;
import theweb.Pages;
import theweb.PathPattern;

public class PageFinder {
    private final static Logger log = Logger.getLogger(PageFinder.class);
    
    public interface Listener {
        void pageFound(Page page);
    }

    private List<Object> dependencies = new ArrayList<Object>();
    private List<String> packagePrefixes = new ArrayList<String>();

    public PageFinder packagePrefix(String packagePrefix) {
        this.packagePrefixes.add(packagePrefix);

        return this;
    }

    public PageFinder dep(Object dep) {
        this.dependencies.add(dep);

        return this;
    }

    public void create(final Pages pages) {
        create(new Listener() {
            @Override
            public void pageFound(Page page) {
                pages.add(page);
            }
        });
    }

    public void create(Listener listener) {
        List<Page> r = new ArrayList<Page>();

        for (String packagePrefix : packagePrefixes) {
            log.debug("Auto creating pages in package " + packagePrefix);

            for (Class<? extends Page> pageClass : PageFinderCache.getPageClasses(packagePrefix)) {
                Page page = createPage(pageClass);

                if (page == null)
                    throw new RuntimeException("Can't create page type " + pageClass.getName());



                r.add(page);
            }
        }

        Collections.sort(r, new PageComparator());

        if (log.isDebugEnabled()) {
            for (Page page : r)
                log.debug("Page " + page.getClass().getName() + ": " + page.getPathPattern().pattern);
        }

        for (Page page : r)
            listener.pageFound(page);
    }
    
    public void preload() {
        for (String packagePrefix : packagePrefixes)
            PageFinderCache.getPageClasses(packagePrefix);
    }
    
    private static class PageComparator implements Comparator<Page> {
        private int getWeight(Page page) {
            PathPattern pathPattern = page.getPathPattern();
            
            String templateTokens[] = pathPattern.pattern.split("/");
            
            return templateTokens.length;
        }
        
        @Override
        public int compare(Page o1, Page o2) {
            return getWeight(o2) - getWeight(o1);
        }
    }

    private Page createPage(Class<? extends Page> pageClass) {
        Page page = null;

        try {
            List<Constructor<?>> cc = Arrays.asList(pageClass.getConstructors());

//            Collections.sort(cc, new Comparator<Constructor<?>>() {
//                @Override
//                public int compare(Constructor<?> o1, Constructor<?> o2) {
//                    return o1.;
//                }
//            });

            for (Constructor c : cc) {
                Object[] args = getConstructorArgs(c);

                if (args == null) continue;

                page = (Page) c.newInstance(args);
                break;
            }
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        }

        return page;
    }

    private Object[] getConstructorArgs(Constructor c) {
        Object[] r = new Object[c.getParameterTypes().length];

        outer:
        for (int i = 0; i < r.length; i ++) {
            for (Object dep : dependencies) {
                if (c.getParameterTypes()[i].isAssignableFrom(dep.getClass())) {
                    r[i] = dep;
                    continue outer;
                }
            }

            return null;
        }

        return r;
    }
}
