package ru.vmsoftware.autoinstall.core.actions;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class NullAction implements Action {

    public static final ActionDefinition<NullAction> DEFINITION = new ActionDefinition<NullAction>() {
        @Override
        public String getName() {
            return "null";
        }

        @Override
        public NullAction getAction() {
            return getInstance();
        }
    };

    public static NullAction getInstance() {
        return instance;
    }

    private NullAction() {
    }

    private static final NullAction instance = new NullAction();

    @Override
    public void execute(ExecutionContext context) throws TaskException {
    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Collections.emptyList();
    }

    @Override
    public ActionDefinition<?> getDefinition() {
        return DEFINITION;
    }
}
