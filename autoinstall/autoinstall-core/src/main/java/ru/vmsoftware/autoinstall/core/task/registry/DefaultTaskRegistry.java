package ru.vmsoftware.autoinstall.core.task.registry;

import ru.vmsoftware.autoinstall.core.task.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultTaskRegistry implements TaskRegistry {

    private static final List<TaskDefinition<?>> DEFINITIONS = Arrays.<TaskDefinition<?>>asList(
            CompositeTask.DEFINITION,
            CopyTask.DEFINITION,
            ExecuteTask.DEFINITION,
            RegistryTask.DEFINITION
    );

    private static final DefaultTaskRegistry instance = new DefaultTaskRegistry();

    public static TaskRegistry getInstance() {
        return instance;
    }

    @Override
    public List<TaskDefinition<?>> getAvailableTasks() {
        return Collections.unmodifiableList(DEFINITIONS);
    }

    private DefaultTaskRegistry() {
    }
}
