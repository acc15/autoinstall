package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CompositeTask extends AbstractTask {

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

    private List<Task> children = new ArrayList<>();
}
