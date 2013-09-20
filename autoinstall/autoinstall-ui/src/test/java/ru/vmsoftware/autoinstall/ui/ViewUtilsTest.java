package ru.vmsoftware.autoinstall.ui;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.TestData;
import ru.vmsoftware.autoinstall.core.TestUtils;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class ViewUtilsTest {

    private static TaskViewModel createEmptyTaskViewModel() {
        final TaskViewModel taskViewModel = new TaskViewModel();
        taskViewModel.getValue().setDescription("");
        taskViewModel.getValue().setConditions("");
        taskViewModel.getValue().setActionType(ActionType.NULL);
        return taskViewModel;
    }

    private static TaskViewModel createTaskViewModel() {
        final TaskViewModel taskViewModel = createEmptyTaskViewModel();
        taskViewModel.getValue().setDescription("String");
        taskViewModel.getValue().setConditions("String");

        final Parameter p1 = new Parameter("abc", "hjuhukio");
        taskViewModel.getValue().getParameters().add(p1);

        final TaskViewModel c1 = createEmptyTaskViewModel();
        c1.setSelected(false);
        c1.getValue().setDescription("test task");
        c1.getValue().setConditions("a == b");
        taskViewModel.getChildren().add(c1);

        final TaskViewModel c11 = createEmptyTaskViewModel();
        c11.getValue().setActionType(ActionType.EXECUTE);

        final Parameter p11 = new Parameter("abc", "hjuhukio");
        c11.getValue().getParameters().add(p11);
        c1.getChildren().add(c11);
        return taskViewModel;
    }

    @Test
    public void testMapViewToDomain() throws Exception {
        final TaskViewModel viewModel = createTaskViewModel();
        final Task expectedTask = TestData.createSampleTask();
        final Task actualTask = ViewUtils.mapViewToDomain(viewModel);
        TestUtils.assertTask(expectedTask, actualTask);
    }

    @Test
    public void testMapDomainToView() throws Exception {

        final Task task = TestData.createSampleTask();
        final TaskViewModel expectedViewModel = createTaskViewModel();
        final TaskViewModel actualViewModel = ViewUtils.mapDomainToView(task);

        // TODO check that expected equal to actual

    }
}
