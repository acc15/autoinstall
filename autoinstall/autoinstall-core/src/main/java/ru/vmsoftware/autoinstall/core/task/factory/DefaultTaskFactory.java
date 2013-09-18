package ru.vmsoftware.autoinstall.core.task.factory;

import ru.vmsoftware.autoinstall.core.task.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultTaskFactory implements TaskFactory {

    private static final List<TaskDefinition<?>> DEFINITIONS = Arrays.<TaskDefinition<?>>asList(
            CompositeTask.DEFINITION,
            CopyTask.DEFINITION,
            ExecuteTask.DEFINITION,
            RegistryTask.DEFINITION
    );

    private static final DefaultTaskFactory instance = new DefaultTaskFactory();

    public static TaskFactory getInstance() {
        return instance;
    }

    @Override
    public List<TaskDefinition<?>> getAvailableTasks() {
        return Collections.unmodifiableList(DEFINITIONS);
    }

    @Override
    public <T extends Task> T createTask(TaskDefinition<T> definition) {
        return definition.createTask();
    }

    private DefaultTaskFactory() {
    }
}
