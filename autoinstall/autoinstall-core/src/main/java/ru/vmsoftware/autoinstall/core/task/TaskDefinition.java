package ru.vmsoftware.autoinstall.core.task;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface TaskDefinition<T extends Task> {

    /**
     * Returns name of task
     * @return name of task
     */
    String getName();

    /**
     * Creates task described by this definition
     * @return new task described by this definition
     */
    T createTask();
}
