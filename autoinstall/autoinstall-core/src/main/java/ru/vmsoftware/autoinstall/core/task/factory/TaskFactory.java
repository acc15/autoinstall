package ru.vmsoftware.autoinstall.core.task.factory;

import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.core.task.TaskDefinition;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface TaskFactory {

    List<TaskDefinition<?>> getAvailableTasks();
    <T extends Task> T createTask(TaskDefinition<T> definition);

}
