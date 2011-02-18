package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;

import theweb.Page;

public interface Execution {
    Object execute() throws IOException;

    Method getMethod();
    Page getPage();
}
