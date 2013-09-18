package ru.vmsoftware.autoinstall.core.task.registry;

import ru.vmsoftware.autoinstall.core.task.TaskDefinition;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface TaskRegistry {

    List<TaskDefinition<?>> getAvailableTasks();

}
