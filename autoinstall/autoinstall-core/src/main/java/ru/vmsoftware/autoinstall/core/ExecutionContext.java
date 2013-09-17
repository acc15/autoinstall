package ru.vmsoftware.autoinstall.core;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public interface ExecutionContext {

    <T> T getParameter(String name, Class<T> type);


    void reportProgress(int value);

    /**
     * Returns {@code true} if installation progress was cancelled and task execution should be interrupted
     * @return {@code true} if current task execution should be interrupted,
     *         {@code false} if execution can continue
     */
    boolean isCancelled();

}
