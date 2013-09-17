package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class RegistryTask extends AbstractLeafTask {

    private static final ParameterDesc<String> REGISTRY_KEY = new ParameterDesc<>("registryKey", String.class);

    @Override
    public void execute(ExecutionContext context) throws TaskException {
        // TODO implement..

    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        // TODO implement..
        return null;
    }
}