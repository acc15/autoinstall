package ru.vmsoftware.autoinstall.core.actions;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class ExecuteAction implements Action {

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
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Arrays.<ParameterDesc<?>>asList(EXECUTABLE, EXECUTION_MODE);
    }

}
