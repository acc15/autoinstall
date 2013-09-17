package ru.vmsoftware.autoinstall.core;

import ru.vmsoftware.autoinstall.core.task.TaskException;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public interface ExecutionContext {

    /**
     * Returns parameter by specified name
     * @param name name of parameter
     * @param type type of parameter
     * @param <T> type of parameter
     * @return value of specified parameter or null
     * @throws TaskException in case of parameter type mismatch
     */
    <T> T getParameter(String name, Class<T> type) throws TaskException;


    void reportProgress(int value);

    /**
     * Returns {@code true} if installation progress was cancelled and task execution should be interrupted
     * @return {@code true} if current task execution should be interrupted,
     *         {@code false} if execution can continue
     */
    boolean isCancelled();

}
