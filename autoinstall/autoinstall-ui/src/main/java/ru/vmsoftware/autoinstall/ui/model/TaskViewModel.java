package ru.vmsoftware.autoinstall.ui.model;

import com.google.common.base.Preconditions;
import ru.vmsoftware.autoinstall.core.actions.ActionDefinition;
import ru.vmsoftware.autoinstall.core.actions.NullAction;
import ru.vmsoftware.autoinstall.core.params.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class TaskViewModel {

    private String description = "";
    private String conditions = "";
    private ActionDefinition<?> actionDefinition = NullAction.DEFINITION;
    private List<Parameter<?>> parameters = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Preconditions.checkNotNull(description);
        this.description = description;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        Preconditions.checkNotNull(conditions);
        this.conditions = conditions;
    }

    public ActionDefinition<?> getActionDefinition() {
        return actionDefinition;
    }

    public void setActionDefinition(ActionDefinition<?> actionDefinition) {
        Preconditions.checkNotNull(actionDefinition);
        this.actionDefinition = actionDefinition;
    }

    public List<Parameter<?>> getParameters() {
        return parameters;
    }

}
