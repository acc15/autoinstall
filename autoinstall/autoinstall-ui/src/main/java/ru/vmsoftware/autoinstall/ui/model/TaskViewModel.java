package ru.vmsoftware.autoinstall.ui.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBoxTreeItem;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.ui.javafx.JavaFxUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class TaskViewModel extends CheckBoxTreeItem<TaskItemModel> {

    public TaskViewModel() {
        final TaskItemModel taskItemModel = new TaskItemModel();
        setValue(taskItemModel);
        setSelected(true);
        setExpanded(true);
        setIndependent(true);
    }
}
