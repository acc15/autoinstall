package ru.vmsoftware.autoinstall.core.task.registry;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.task.*;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultTaskFactoryTest {

    private TaskRegistry factory = DefaultTaskRegistry.getInstance();

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

}
