package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.action.Action;
import ru.vmsoftware.autoinstall.core.action.NullAction;
import ru.vmsoftware.autoinstall.core.params.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class Task {

    private boolean active = true;
    private String description = "";
    private List<Task> children;
    private List<Parameter<?>> parameters = new ArrayList<>();
    private Action action = NullAction.getInstance();

    public Task() {
        this(new ArrayList<Task>());
    }

    public Task(List<Task> children) {
        this.children = children;
    }

    /**
     * Returns whether this task is active or not
     * @return {@code true} if this task is active and should be executed
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates or deactivates current task
     * @param value activation status: {@code true} to active, {@code false} inactive
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Returns action for this task
     * @return action for task or {@code null} if this is composite task
     */
    public Action getAction() {
        return action;
    }

    /**
     * Sets action for this task
     * @param action new action for task
     */
    public void setAction(Action action) {
        if (action == null) {
            throw new NullPointerException("task action can't be null");
        }
        this.action = action;
    }

    /**
     * Returns custom description of this task
     * @return description of this task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description for this task
     * @param description new description for this task
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new NullPointerException("task description can't be null");
        }
        this.description = description;
    }

    /**
     * Returns approximately cost of this task (this parameter is required for progress bar processing)
     * @return approximate cost of this task
     */
    //int getCost(ExecutionContext context) throws TaskException;

    /**
     * Executes task
     * @param context context for getting parameters and reporting progress
     */
    public void execute(ExecutionContext context) throws TaskException {
        if (!active || context.isCancelled()) {
            return;
        }

        // TODO add parameters from this task
        final ExecutionContext newContext = context;

        // TODO check for conditions

        action.execute(newContext);
        for (final Task child: children) {
            child.execute(newContext);
            if (context.isCancelled()) {
                return;
            }
        }
    }

    /**
     * Returns list of children tasks if current task is composite.
     * @return list of children tasks or null if current task is a leaf
     */
    public List<Task> getChildren() {
        return children;
    }

    /**
     * Returns list of parameters which current task declares.
     * Note that returned list can be modified and provides
     * interface for adding, removing and modifying task parameters.
     * @return modifiable list of parameters.
     */
    public List<Parameter<?>> getParameters() {
        return parameters;
    }


}
