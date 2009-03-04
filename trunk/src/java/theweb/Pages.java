package theweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Pages {
    private List<Page> pages = new ArrayList<Page>();

    public void addPage(Page page) {
        this.pages.add(page);
    }
    
    private List<PageInterceptor> interceptors = new ArrayList<PageInterceptor>();
    
    public void addInterceptor(PageInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new ContextInfo(request.getContextPath() + request.getServletPath());
        
        Page page = getPage(request);
        
        if (page == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        for (PageInterceptor interceptor : interceptors) {
            if (!interceptor.beforePopulate(page, request, response))
                return;
        }
        
        
        try {
            new Populator().populate(page, request);
            
            PageState.setCurrent(new PageState(page));
            
            Outcome outcome = new Executor().exec(page, request);
            
            outcome.process(page, request, response);
        } finally {
            PageState.clear();
        }
    }

    private Page getPage(HttpServletRequest request) {
        String path = request.getPathInfo();
        
        if (path == null) path = "";
        
        for (Page page : pages)
            if (path.startsWith(page.baseUrl)) 
                return page;
        
        return null;
    }
}
