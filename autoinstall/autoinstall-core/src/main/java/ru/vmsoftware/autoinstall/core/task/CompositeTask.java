package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CompositeTask extends AbstractTask {

    @Override
    public void execute(ExecutionContext context) throws TaskException{
        for (Task child: children) {
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
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Collections.emptyList();
    }

    private List<Task> children = new ArrayList<>();
}
