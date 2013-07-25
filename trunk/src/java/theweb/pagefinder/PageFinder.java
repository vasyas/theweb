package theweb.pagefinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import theweb.Page;
import theweb.Pages;
import theweb.PathPattern;

public class PageFinder {
    private final static Logger log = Logger.getLogger(PageFinder.class);
    
    public interface Listener {
        void pageFound(Page page);
    }
    
    public static void create(String packagePrefix, final Pages pages) {
        create(packagePrefix, new Listener() {
            @Override
            public void pageFound(Page page) {
                pages.add(page);
            }
        });
    }
    
    public static void create(String packagePrefix, Listener listener) {
        List<Page> r = new ArrayList<Page>();
        
        for (Class<? extends Page> pageClass : PageFinderCache.getPageClasses(packagePrefix)) {
            Page page = createPage(pageClass);
            
            if (page == null) 
                throw new RuntimeException("Can't create page type " + pageClass.getName());
            
            r.add(page);
        }
        
        Collections.sort(r, new PageComparator());
        
        if (log.isDebugEnabled()) {
            log.debug("Auto creating pages in package " + packagePrefix);
            
            for (Page page : r)
                log.debug("Page " + page.getClass().getName() + ": " + page.getPathPattern().pattern);
        }
        
        for (Page page : r)
            listener.pageFound(page);
    }
    
    public static void init(String ... packagePrefixes) {
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

    private static Page createPage(Class<? extends Page> pageClass) {
        try {
            Page page = pageClass.newInstance();
            
            return page;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
