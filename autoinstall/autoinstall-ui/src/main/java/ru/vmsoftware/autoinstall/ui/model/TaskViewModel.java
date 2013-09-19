package ru.vmsoftware.autoinstall.ui.model;

import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model which is a mirror of {@link ru.vmsoftware.autoinstall.core.task.Task} domain model
 * but designed to work with JavaFX UI components.
 *
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class TaskViewModel {

    private String description = "";
    private String conditions = "";
    private ActionType actionType = ActionType.NULL;
    private List<Parameter> parameters = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
