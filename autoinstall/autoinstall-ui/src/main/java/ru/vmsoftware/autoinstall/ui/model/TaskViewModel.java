package ru.vmsoftware.autoinstall.ui.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;

/**
 * Model which is a mirror of {@link ru.vmsoftware.autoinstall.core.task.Task} domain model
 * but designed to work with JavaFX UI components.
 *
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class TaskViewModel {

    private StringProperty description = new SimpleStringProperty();
    private StringProperty conditions = new SimpleStringProperty();
    private ObjectProperty<ActionType> actionType = new SimpleObjectProperty<>();
    private ObservableList<Parameter> parameters = new SimpleListProperty<>();

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty conditionsProperty() {
        return conditions;
    }

    public String getConditions() {
        return conditions.get();
    }

    public void setConditions(String conditions) {
        this.conditions.set(conditions);
    }

    public ObjectProperty<ActionType> actionTypeProperty() {
        return actionType;
    }

    public ActionType getActionType() {
        return actionType.get();
    }

    public void setActionType(ActionType actionType) {
        this.actionType.set(actionType);
    }

    public ObservableList<Parameter> getParameters() {
        return parameters;
    }

}
