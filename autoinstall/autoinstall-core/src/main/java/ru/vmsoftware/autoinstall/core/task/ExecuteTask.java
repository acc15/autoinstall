package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.io.File;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class ExecuteTask implements Task {

    private static final ParameterDesc<File> EXECUTABLE = new ParameterDesc<>("executable", File.class);
    private static final ParameterDesc<ExecutionMode> EXECUTION_MODE = new ParameterDesc<>("executionMode",
            ExecutionMode.class, ExecutionMode.WAIT_FOR_FINISH);

    public static enum ExecutionMode {

        WAIT_FOR_FINISH,
        ASYNCHRONOUS

    }



    @Override
    public void execute(ExecutionContext context) throws TaskException {

       // Runtime.getRuntime().ex
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
