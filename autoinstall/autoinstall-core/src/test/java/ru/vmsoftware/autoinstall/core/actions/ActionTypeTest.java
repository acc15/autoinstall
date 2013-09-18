package ru.vmsoftware.autoinstall.core.actions;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class ActionTypeTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAvailableTasksNotModifiable() throws Exception {
        final List<ActionType> definitions = ActionType.getAvailableActions();
        definitions.set(0, ActionType.EXECUTE);
    }

    @Test
    public void testGetAvailableTasks() throws Exception {
        final List<ActionType> definitions = ActionType.getAvailableActions();
        assertThat(definitions).<ActionType>containsExactly(ActionType.values());
    }

    @Test
    public void testGetDefinitionByNameShouldReturnNullIfNameIsNotKnown() throws Exception {
        assertThat(ActionType.getDefinitionByName("abc")).isNull();
    }

    @Test
    public void testGetDefinitionByNameShouldReturnCorrectDefinitions() throws Exception {
        for (ActionType def: ActionType.values()) {
            assertThat(ActionType.getDefinitionByName(def.getName())).isSameAs(def);
        }
    }
}
