package ru.vmsoftware.autoinstall.ui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.vmsoftware.autoinstall.core.actions.ActionType;

/**
 * Model which is a mirror of {@link ru.vmsoftware.autoinstall.core.task.Task} domain model
 * but designed to work with JavaFX UI components.
 *
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class TaskItemModel {

    private StringProperty description = new SimpleStringProperty("");
    private StringProperty conditions = new SimpleStringProperty("");
    private ObjectProperty<ActionType> actionType = new SimpleObjectProperty<>(ActionType.NULL);
    private ObservableList<ParameterViewModel> parameters = FXCollections.observableArrayList();

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

    public ObservableList<ParameterViewModel> getParameters() {
        return parameters;
    }

}
