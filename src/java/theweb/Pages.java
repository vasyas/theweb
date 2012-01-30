package theweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public void invoke(HttpExchange exchange) throws IOException {
        new ContextInfo(exchange.getContextPath());
        
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        
        Page page = getPage(exchange, properties);
        
        if (page == null) {
            exchange.sendError(404);
            return;
        }
        
        try {
            for (Collector collector : collectors)
                collector.collect(properties, exchange);
            
            populator.populate(page, properties);
            
            PageState.setCurrent(new PageState(page));
        
            Object result = new Executor(interceptors).exec(page, properties, exchange);
            
            if (result instanceof Outcome) 
                ((Outcome) result).process(page, exchange);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            PageState.clear();
        }
    }

    private Page getPage(HttpExchange exchange, Map<String, Object> properties) {
        String path = exchange.getRequestPath();
        
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
