package ru.vmsoftware.autoinstall.core.action;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class RegistryAction implements Action {

    public static final ActionDefinition<RegistryAction> DEFINITION = new ActionDefinition<RegistryAction>() {
        @Override
        public String getName() {
            return "registry";
        }

        @Override
        public RegistryAction getAction() {
            return new RegistryAction();
        }
    };

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

    @Override
    public ActionDefinition<?> getDefinition() {
        return DEFINITION;
    }
}
