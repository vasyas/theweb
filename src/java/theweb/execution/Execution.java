package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;

import theweb.Page;

public interface Execution {
    Object execute() throws IOException;

    Object[] getArgs();
    Method getMethod();
    Page getPage();
}
