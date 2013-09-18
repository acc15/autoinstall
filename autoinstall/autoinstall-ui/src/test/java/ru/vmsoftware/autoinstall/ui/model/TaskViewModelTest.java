package ru.vmsoftware.autoinstall.ui.model;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.actions.NullAction;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class TaskViewModelTest {

    @Test
    public void testTaskHasNullActionDefinitionByDefault() throws Exception {
        final TaskViewModel task = new TaskViewModel();
        assertThat(task.getActionDefinition()).isEqualTo(NullAction.DEFINITION);
    }



}
