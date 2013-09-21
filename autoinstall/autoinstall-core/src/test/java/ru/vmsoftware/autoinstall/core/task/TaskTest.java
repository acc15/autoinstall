package ru.vmsoftware.autoinstall.core.task;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class TaskTest {

    @Test
    public void testDescriptionMethodsWorksCorrectly() throws Exception {
        final Task testTask = new Task();
        assertThat(testTask.getDescription()).isEmpty();
        final String expectedDescription = "test description";
        testTask.setDescription(expectedDescription);
        assertThat(testTask.getDescription()).isEqualTo(expectedDescription);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDescriptionThrowsExceptionOnNull() throws Exception {
        final Task testTask = new Task();
        testTask.setDescription(null);
    }

    @Test
    public void testGetParametersReturnsModifiableList() throws Exception {
        final Task testTask = new Task();
        final Parameter expectedParameter = new Parameter("test", "abc");
        testTask.getParameters().add(expectedParameter);
        assertThat(testTask.getParameters()).containsExactly(expectedParameter);

        testTask.getParameters().remove(0);
        assertThat(testTask.getParameters()).isEmpty();
    }

    @Test
    public void testTaskIsActiveByDefault() throws Exception {
        final Task testTask = new Task();
        assertThat(testTask.isActive()).isTrue();
    }

    @Test
    public void testSetActiveModifiesActiveStatus() throws Exception {
        final Task testTask = new Task();
        testTask.setActive(false);
        assertThat(testTask.isActive()).isFalse();
    }

    @Test
    public void testGetChildrenReturnsModifiableList() throws Exception {
        final Task task = new Task();
        final Task expectedTask = new Task();
        task.getChildren().add(expectedTask);
        assertThat(task.getChildren()).containsExactly(expectedTask);
    }

    @Test(expected = NullPointerException.class)
    public void testActionDefinitionCantBeNull() throws Exception {
        final Task task = new Task();
        task.setActionType(null);
    }

    @Test
    public void testTaskHasNullActionByDefault() throws Exception {
        assertThat(new Task().getActionType()).isSameAs(ActionType.NULL);
    }

    @Test
    public void testActionDefinitionCanBeModified() throws Exception {
        final Task task = new Task();
        final ActionType expectedDef = ActionType.COPY;
        task.setActionType(expectedDef);
        assertThat(task.getActionType()).isSameAs(expectedDef);
    }

    @Test
    public void testConditionsIsEmptyByDefault() throws Exception {
        assertThat(new Task().getConditions()).isEmpty();
    }

    @Test
    public void testConditionsCanBeModified() throws Exception {
        final Task task = new Task();
        final String expectedConditions = "test condition";
        task.setConditions(expectedConditions);
        assertThat(task.getConditions()).isEqualTo(expectedConditions);
    }

    @Test(expected = NullPointerException.class)
    public void testConditionsCantBeNull() throws Exception {
        new Task().setConditions(null);
    }
}
