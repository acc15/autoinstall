package ru.vmsoftware.autoinstall.ui;

import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.ui.model.ParameterViewModel;
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
        for (final ParameterViewModel parameterViewModel: viewModel.getValue().getParameters()) {
            task.getParameters().add(new Parameter(parameterViewModel.getName(), parameterViewModel.getValue()));
        }

        final List<TaskViewModel> children = (List<TaskViewModel>)(List<?>) viewModel.getChildren();
        for (final TaskViewModel child: children) {
            task.getChildren().add(mapViewToDomain(child));
        }
        return task;
    }

    public static TaskViewModel mapDomainToView(Task task) {
        final TaskViewModel viewModel = new TaskViewModel();
        viewModel.setSelected(task.isActive());
        viewModel.getValue().setActionType(task.getActionType());
        viewModel.getValue().setConditions(task.getConditions());
        viewModel.getValue().setDescription(task.getDescription());

        for (final Parameter parameter: task.getParameters()) {
            final ParameterViewModel parameterViewModel = new ParameterViewModel();
            parameterViewModel.setName(parameter.getName());
            parameterViewModel.setValue(parameter.getValue());
            viewModel.getValue().getParameters().add(parameterViewModel);
        }

        final List<Task> children = task.getChildren();
        for (final Task child: children) {
            final TaskViewModel viewChild = mapDomainToView(child);
            viewModel.getChildren().add(viewChild);
        }
        return viewModel;
    }

}
