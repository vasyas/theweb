package theweb.execution;

import java.io.IOException;

import theweb.HttpExchange;

public interface PageInterceptor {
    Object execute(Execution execution, HttpExchange exchange) throws IOException;
}
