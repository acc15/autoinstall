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
     * Returns whether this task is active or not
     * @return {@code true} if this task is active and should be executed
     */
    boolean isActive();

    /**
     * Activates or deactivates current task
     * @param value activation status: {@code true} to active, {@code false} inactive
     */
    void setActive(boolean value);

    /**
     * Returns custom description of this task
     * @return description of this task
     */
    String getDescription();

    /**
     * Sets description for this task
     * @param description new description for this task
     */
    void setDescription(String description);

    /**
     * Returns approximately cost of this task (this parameter is required for progress bar processing)
     * @return approximate cost of this task
     */
    //int getCost(ExecutionContext context) throws TaskException;

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
     * Returns list of all parameters
     * which current task can accept (but this doesn't mean that all of it will be used).
     * Note that composite tasks should not inherit this definitions from children. By the way returned list
     * must be either immutable or task should build this list every time definitions is request. This means that
     * this method should be implemented in a way which permits modification of task internal state
     * @return list of parameter descriptions
     */
    List<ParameterDesc<?>> getParameterDefinitions();

    /**
     * Returns list of parameters which current task declares.
     * Note that returned list can be modified and provides
     * interface for adding, removing and modifying task parameters.
     * @return modifiable list of parameters.
     */
    List<Parameter<?>> getParameters();

    /**
     * Returns current task definition
     * @return task definition
     */
    TaskDefinition<?> getDefinition();
}
