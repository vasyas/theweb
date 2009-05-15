package theweb.execution;

import java.io.IOException;
import java.lang.reflect.Method;

import theweb.Outcome;
import theweb.Page;

public interface Execution {
    Outcome execute() throws IOException;

    Method getMethod();
    Page getPage();
}
