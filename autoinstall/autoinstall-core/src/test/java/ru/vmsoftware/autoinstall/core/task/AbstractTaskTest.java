package ru.vmsoftware.autoinstall.core.task;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class AbstractTaskTest {

    private static class TestTask extends AbstractTask {
        @Override
        public void execute(ExecutionContext context) throws TaskException {
        }

        @Override
        public List<Task> getChildren() {
            return null;
        }

        @Override
        public TaskDefinition<?> getDefinition() {
            return null;
        }
    }

    @Test
    public void testDescriptionMethodsWorksCorrectly() throws Exception {
        final TestTask testTask = new TestTask();
        assertThat(testTask.getDescription()).isEmpty();
        final String expectedDescription = "test description";
        testTask.setDescription(expectedDescription);
        assertThat(testTask.getDescription()).isEqualTo(expectedDescription);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDescriptionThrowsExceptionOnNull() throws Exception {
        final TestTask testTask = new TestTask();
        testTask.setDescription(null);
    }

    @Test
    public void testGetParametersReturnsModifiableList() throws Exception {
        final TestTask testTask = new TestTask();
        final Parameter<String> expectedParameter = new Parameter<>("test", "abc");
        testTask.getParameters().add(expectedParameter);
        assertThat(testTask.getParameters()).containsExactly(expectedParameter);

        testTask.getParameters().remove(0);
        assertThat(testTask.getParameters()).isEmpty();
    }

    @Test
    public void testGetParameterDefinitionsReturnsEmptyList() throws Exception {
        final TestTask testTask = new TestTask();
        assertThat(testTask.getParameterDefinitions()).isEmpty();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetParameterDefinitionsReturnsUnmodifiableList() throws Exception {
        final TestTask testTask = new TestTask();
        testTask.getParameterDefinitions().add(new ParameterDesc<>("test", TestTask.class));
    }

    @Test
    public void testTaskIsActiveByDefault() throws Exception {
        final TestTask testTask = new TestTask();
        assertThat(testTask.isActive()).isTrue();
    }

    @Test
    public void testSetActiveModifiesActiveStatus() throws Exception {
        final TestTask testTask = new TestTask();
        testTask.setActive(false);
        assertThat(testTask.isActive()).isFalse();
    }
}
