package ru.vmsoftware.autoinstall.core.actions;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class DefaultActionRegistryTest {

    private ActionRegistry registry = DefaultActionRegistry.getInstance();

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAvailableTasksNotModifiable() throws Exception {
        final List<ActionDefinition<?>> definitions = registry.getAvailableActions();
        definitions.set(0, CopyAction.DEFINITION);
    }

    @Test
    public void testGetAvailableTasks() throws Exception {
        final List<ActionDefinition<?>> definitions = registry.getAvailableActions();
        assertThat(definitions).<ActionDefinition<?>>contains(
                CopyAction.DEFINITION,
                RegistryAction.DEFINITION,
                ExecuteAction.DEFINITION);
    }

}
