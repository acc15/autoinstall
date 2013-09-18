package ru.vmsoftware.autoinstall.core.task.factory;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.task.*;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultTaskFactoryTest {

    private TaskFactory factory = DefaultTaskFactory.getInstance();

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAvailableTasksNotModifiable() throws Exception {
        final List<TaskDefinition<?>> definitions = factory.getAvailableTasks();
        definitions.set(0, CopyTask.DEFINITION);
    }

    @Test
    public void testGetAvailableTasks() throws Exception {
        final List<TaskDefinition<?>> definitions = factory.getAvailableTasks();
        assertThat(definitions).<TaskDefinition<?>>contains(
                CompositeTask.DEFINITION,
                CopyTask.DEFINITION,
                RegistryTask.DEFINITION,
                ExecuteTask.DEFINITION);
    }

    @Test
    public void testCreateTask() throws Exception {
        final CopyTask copyTask = factory.createTask(CopyTask.DEFINITION);
        assertThat(copyTask).isInstanceOf(CopyTask.class);
    }
}
