package theweb;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class InterceptedExecution implements Execution {
    private Iterator<PageInterceptor> current;
    private Execution lastExecution;

    private HttpExchange exchange;

    public InterceptedExecution(List<PageInterceptor> interceptors, HttpExchange exchange, Execution last) {
        this.current = interceptors.iterator();
        
        this.lastExecution = last;
        this.exchange = exchange;
    }

    public Object execute() throws Exception {
        if ( current.hasNext() ) {
            PageInterceptor interceptor = current.next();
            return interceptor.execute(this, exchange);
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
    
    @Override
    public Object[] getArgs() {
        return lastExecution.getArgs();
    }
}