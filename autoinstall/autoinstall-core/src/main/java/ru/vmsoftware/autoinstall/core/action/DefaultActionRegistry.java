package ru.vmsoftware.autoinstall.core.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultActionRegistry implements ActionRegistry {

    private static final List<ActionDefinition<?>> DEFINITIONS = Arrays.asList(
            NullAction.DEFINITION,
            CopyAction.DEFINITION,
            ExecuteAction.DEFINITION,
            RegistryAction.DEFINITION
    );

    private static final DefaultActionRegistry instance = new DefaultActionRegistry();

    public static ActionRegistry getInstance() {
        return instance;
    }

    @Override
    public List<ActionDefinition<?>> getAvailableActions() {
        return Collections.unmodifiableList(DEFINITIONS);
    }

    private DefaultActionRegistry() {
    }
}
