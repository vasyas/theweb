package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import theweb.Page;

class ChainedActionExecution implements Execution {
    private Iterator<PageInterceptor> current;
    private Execution lastExecution;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ChainedActionExecution(List<PageInterceptor> interceptors, HttpServletRequest request, HttpServletResponse response, Execution last) {
        this.current = interceptors.iterator();
        
        this.lastExecution = last;
        this.request = request;
        this.response = response;
    }

    public Object execute() throws IOException {
        if ( current.hasNext() ) {
            PageInterceptor interceptor = current.next();
            return interceptor.execute(this, request, response);
        }

        return lastExecution.execute();
    }

    @Override
    public Method getMethod() {
        return lastExecution.getMethod();
    }

    @Override
    public Page getPage() {
        return lastExecution.getPage();
    }
}