package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public interface Task {

    /**
     * Executes task
     * @param context context for getting parameters and reporting progress
     */
    void execute(ExecutionContext context) throws TaskException;

    /**
     * Returns list of children tasks if current task is composite.
     * @return list of children tasks or null if current task is a leaf
     */
    List<Task> getChildren();

    /**
     * Returns {@link java.util.Collections#unmodifiableList(java.util.List) unmodifiable} list of all parameters
     * which current task can accept (but this doesn't mean that all of it will be used).
     * Note that composite tasks should not inherit this definitions
     * @return {@link java.util.Collections#unmodifiableList(java.util.List) unmodifiable} list of parameters description
     */
    List<ParameterDesc<?>> getParameterDefinitions();

    /**
     * Returns list of parameters which current task declares.
     * @return
     */
    List<Parameter<?>> getParameters();

}
