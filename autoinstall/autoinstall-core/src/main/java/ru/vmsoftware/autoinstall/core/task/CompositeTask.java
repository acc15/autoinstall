package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CompositeTask implements Task {

    @Override
    public void execute(ExecutionContext context) {
        // TODO implement..

    }

    @Override
    public List<Task> getChildren() {
        // TODO implement..
        return null;
    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        // TODO implement..
        return null;
    }

    @Override
    public List<Parameter<?>> getParameters() {
        // TODO implement..
        return null;
    }
}
