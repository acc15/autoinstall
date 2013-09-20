package ru.vmsoftware.autoinstall.ui;

import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class ViewUtils {

    @SuppressWarnings("unchecked")
    public static Task mapViewToDomain(TaskViewModel viewModel) {
        final Task task = new Task();
        task.setActive(viewModel.isSelected());
        task.setActionType(viewModel.getValue().getActionType());
        task.setDescription(viewModel.getValue().getDescription());
        task.setConditions(viewModel.getValue().getConditions());
        task.getParameters().addAll(viewModel.getValue().getParameters());
        final List<TaskViewModel> children = (List<TaskViewModel>)(List<?>) viewModel.getChildren();
        for (final TaskViewModel child: children) {
            task.getChildren().add(mapViewToDomain(child));
        }
        return task;
    }

    public static TaskViewModel mapDomainToView(Task task) {
        final TaskViewModel viewModel = new TaskViewModel();
        viewModel.setSelected(task.isActive());
        // TODO ViewModel should be created with attached listeners...
        //viewModel.set
        return null;
    }

}
