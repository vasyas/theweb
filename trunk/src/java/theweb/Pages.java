package theweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.execution.Executor;
import theweb.execution.PageInterceptor;

public class Pages {
    private List<Page> pages = new ArrayList<Page>();
    
    public Pages add(Page page) {
        return addPage(page);
    }
    
    public Pages addPage(Page page) {
        this.pages.add(page);
        return this;
    }
    
    private List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
    
    public void addInterceptor(PageInterceptor interceptor) {
        interceptors.add(interceptor);
    }
    
    private List<Collector> collectors = new ArrayList<Collector>();
    
    public void addCollector(Collector collector) {
        collectors.add(collector);
    }
    
    public Populator populator = new ReflectionPopulator(true);

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new ContextInfo(request.getContextPath());
        
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        
        Page page = getPage(request, properties);
        
        if (page == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        try {
            for (Collector collector : collectors)
                collector.collect(properties, request);
            
            populator.populate(page, properties);
            
            PageState.setCurrent(new PageState(page));
        
            Outcome outcome = new Executor(interceptors).exec(page, properties, request, response);
            
            outcome.process(page, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            PageState.clear();
        }
    }

    private Page getPage(HttpServletRequest request, Map<String, Object> properties) {
        String path = request.getServletPath() + request.getPathInfo();
        
        if (path == null) path = "";
        
        for (Page page : pages) {
            Map<String, String> matches = page.getPathPattern().matches(path);
            
            if (matches != null) {
                properties.putAll(matches);
                
                return page;
            }
        }
        
        return null;
    }
}
