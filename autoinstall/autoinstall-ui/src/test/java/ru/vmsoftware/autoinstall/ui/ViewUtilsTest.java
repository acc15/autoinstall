package ru.vmsoftware.autoinstall.ui;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.TestData;
import ru.vmsoftware.autoinstall.core.TestUtils;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.ui.model.TaskItemModel;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

import static org.fest.assertions.Assertions.assertThat;

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

    public static void assertTaskViewModel(TaskViewModel expectedTask, TaskViewModel actualTask, String prefix) {

        final String itemPrefix = prefix + ".value";
        final TaskItemModel expectedItemModel = expectedTask.getValue();
        assertThat(actualTask).as(prefix).isNotNull();

        final TaskItemModel actionItemModel = actualTask.getValue();
        assertThat(actionItemModel.getDescription()).as(itemPrefix + ".description").
                isEqualTo(expectedItemModel.getDescription());
        assertThat(actionItemModel.getConditions()).as(itemPrefix + ".conditions").
                isEqualTo(expectedItemModel.getConditions());
        assertThat(actionItemModel.getActionType()).as(itemPrefix + ".actionType").
                isEqualTo(expectedItemModel.getActionType());
        assertThat(actualTask.isSelected()).as(prefix + ".selected").
                isEqualTo(expectedTask.isSelected());

        assertThat(actionItemModel.getParameters()).as(itemPrefix + "parameters.size").
                hasSize(expectedItemModel.getParameters().size());

        for (int i=0; i<expectedItemModel.getParameters().size(); i++) {
            final Parameter expectedParameter = expectedItemModel.getParameters().get(i);
            final Parameter actualParameter = actionItemModel.getParameters().get(i);
            assertThat(expectedParameter.getName()).as(itemPrefix + ".parameter[" + i + "].name").
                    isEqualTo(actualParameter.getName());
            assertThat(expectedParameter.getValue()).as(itemPrefix + ".parameter[" + i + "].value").
                    isEqualTo(actualParameter.getValue());
        }

        assertThat(actualTask.getChildren()).hasSize(expectedTask.getChildren().size());
        for (int i=0; i<expectedTask.getChildren().size(); i++) {
            final TaskViewModel expectedChild = (TaskViewModel)expectedTask.getChildren().get(i);
            final TaskViewModel actualChild = (TaskViewModel)actualTask.getChildren().get(i);
            assertTaskViewModel(expectedChild, actualChild, prefix + ".children[" + i + "]");
        }

    }

    public static void assertTaskViewModel(TaskViewModel expectedTask, TaskViewModel actualTask) {
        assertTaskViewModel(expectedTask, actualTask, "task");
    }

    @Test
    public void testMapViewToDomainShouldCopyParameters() throws Exception {
        final TaskViewModel viewModel = createTaskViewModel();
        final Task task = ViewUtils.mapViewToDomain(viewModel);
        viewModel.getValue().getParameters().get(0).setName("test");
        assertThat(task.getParameters().get(0).getName()).isEqualTo("abc");
    }

    @Test
    public void testMapDomainToViewShouldCopyParameters() throws Exception {
        final Task task = TestData.createSampleTask();
        final TaskViewModel viewModel = ViewUtils.mapDomainToView(task);
        task.getParameters().get(0).setName("test");
        assertThat(viewModel.getValue().getParameters().get(0).getName()).isEqualTo("abc");
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
        assertTaskViewModel(expectedViewModel, actualViewModel);
    }
}
