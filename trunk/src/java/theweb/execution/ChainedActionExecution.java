package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import theweb.HttpExchange;
import theweb.Page;

class ChainedActionExecution implements Execution {
    private Iterator<PageInterceptor> current;
    private Execution lastExecution;

    private HttpExchange exchange;

    public ChainedActionExecution(List<PageInterceptor> interceptors, HttpExchange exchange, Execution last) {
        this.current = interceptors.iterator();
        
        this.lastExecution = last;
        this.exchange = exchange;
    }

    public Object execute() throws IOException {
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