package theweb;

public interface Page {
    Markup markup() throws Exception;
    PathPattern getPathPattern();
}