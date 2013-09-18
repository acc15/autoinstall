package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CompositeTask extends AbstractTask {

    public static final TaskDefinition<CompositeTask> DEFINITION = new TaskDefinition<CompositeTask>() {
        @Override
        public String getName() {
            return "composite";
        }

        @Override
        public CompositeTask createTask() {
            return new CompositeTask();
        }
    };

    @Override
    public void execute(ExecutionContext context) throws TaskException{
        for (final Task child: children) {
            if (context.isCancelled()) {
                return;
            }
            child.execute(context);
        }
    }

    @Override
    public List<Task> getChildren() {
        return children;
    }

    @Override
    public TaskDefinition<?> getDefinition() {
        return DEFINITION;
    }

    private List<Task> children = new ArrayList<>();
}
