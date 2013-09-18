package ru.vmsoftware.autoinstall.core.action;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface ActionDefinition<T extends Action> {

    /**
     * Returns name of task
     * @return name of task
     */
    String getName();

    /**
     * Creates task described by this definition
     * @return new task described by this definition
     */
    T getAction();
}
