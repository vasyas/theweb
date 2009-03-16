package theweb.execution;

import java.lang.reflect.Method;

import theweb.Outcome;
import theweb.Page;

public interface Execution {
    Outcome execute();

    Method getMethod();
    Page getPage();
}
