package ru.vmsoftware.autoinstall.core.task;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.action.CopyAction;
import ru.vmsoftware.autoinstall.core.action.NullAction;
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
        final Parameter<String> expectedParameter = new Parameter<>("test", "abc");
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
    public void testActionCantBeNull() throws Exception {
        final Task task = new Task();
        task.setAction(null);
    }

    @Test
    public void testTaskHasNullActionByDefault() throws Exception {
        final Task task = new Task();
        assertThat(task.getAction()).isInstanceOf(NullAction.class);
    }

    @Test
    public void testActionCanBeModified() throws Exception {
        final Task task = new Task();
        final CopyAction expectedAction = new CopyAction();
        task.setAction(expectedAction);
        assertThat(task.getAction()).isSameAs(expectedAction);
    }
}
