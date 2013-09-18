package ru.vmsoftware.autoinstall.core.action;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface Action {

    /**
     * Executes task
     * @param context context for getting parameters and reporting progress
     */
    void execute(ExecutionContext context) throws TaskException;

    /**
     * Returns list of all parameters
     * which current task can accept (but this doesn't mean that all of it will be used).
     * Note that composite tasks should not inherit this definitions from children. By the way returned list
     * must be either immutable or task should build this list every time definitions is request. This means that
     * this method should be implemented in a way which permits modification of task internal state
     * @return list of parameter descriptions
     */
    List<ParameterDesc<?>> getParameterDefinitions();


    /**
     * Returns current task definition
     * @return task definition
     */
    ActionDefinition<?> getDefinition();

}
