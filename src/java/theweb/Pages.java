package theweb;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Pages {
    private final static Logger log = Logger.getLogger(Pages.class);

    private List<Page> pages = new ArrayList<Page>();
    
    public Pages(Page... pages) {
        Stream.of(pages).forEach(this::add);
    }

    public Pages add(Page page) {
        pages.add(page);
        return this;
    }

    private List<MethodMatcher> methodMatchers = new ArrayList<MethodMatcher>(Arrays.asList(
            new NameMethodMatcher(), new DefaultMethodMatcher()
    ));

    private List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();

    public Pages interceptors(PageInterceptor... interceptors) {
        Stream.of(interceptors).forEach(this.interceptors::add);
        return this;
    }

    private List<Collector> collectors = new ArrayList<Collector>();

    public Pages collectors(Collector ... collectors) {
        Stream.of(collectors).forEach(this.collectors::add);
        return this;
    }

    public void invoke(HttpExchange exchange) throws IOException {
        Map<String, Object> properties = new LinkedHashMap<>();
        
        Page page = getPage(exchange, properties);
        
        if (page == null) {
            exchange.sendError(404);
            return;
        }
        
        try {
            for (Collector collector : collectors)
                collector.collect(properties, exchange);

            new Populator().populate(page, properties);
            
            PageState.setCurrent(new PageState(page));
        
            Object result = exec(page, properties, exchange);

            if (result != null)
                log.warn("Unhandled " + result + " from " + page);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            PageState.clear();
        }
    }

    Object exec(Page page, Map<String, Object> properties, HttpExchange exchange) throws Exception {
        Method method = getMethod(page, exchange, properties);

        if (method == null) return Response.NotFound;

        MethodExecution methodExecution = new MethodExecution(page, method, properties);

        InterceptedExecution interceptedExecution = new InterceptedExecution(interceptors, exchange, methodExecution);

        return interceptedExecution.execute();
    }

    private Page getPage(HttpExchange exchange, Map<String, Object> properties) {
        String path = exchange.getRequestPath();
        
        for (Page page : pages) {
            PathPattern.Match match = new PathPattern(page.path).match(path);
            
            if (match.matched()) {
                properties.putAll(match.getVars());
                
                return page;
            }
        }
        
        return null;
    }

    private Method getMethod(Page page, HttpExchange exchange, Map<String, Object> properties) {
        for (MethodMatcher methodMatcher : methodMatchers) {
            Method method = methodMatcher.getMethod(page, exchange, properties);

            if (method != null) return method;
        }

        return null;
    }
}
