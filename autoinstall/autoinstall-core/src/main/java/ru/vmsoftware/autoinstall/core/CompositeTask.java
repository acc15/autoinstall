package ru.vmsoftware.autoinstall.core;

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
