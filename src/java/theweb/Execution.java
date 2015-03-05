package theweb;

import java.lang.reflect.Method;

public interface Execution {
    Object execute() throws Exception;

    Object[] getArgs();
    Method getMethod();
    Page getPage();
}
