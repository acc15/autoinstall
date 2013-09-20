package ru.vmsoftware.autoinstall.core;

import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.task.Task;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class TestUtils {
    public static void assertTask(Task expectedTask, Task actualTask, String prefix) {

        assertThat(actualTask.getDescription()).as(prefix + ".description").
                isEqualTo(expectedTask.getDescription());
        assertThat(actualTask.getConditions()).as(prefix + ".conditions").
                isEqualTo(expectedTask.getConditions());
        assertThat(actualTask.getActionType()).as(prefix + ".actionType").
                isEqualTo(expectedTask.getActionType());
        assertThat(actualTask.isActive()).as(prefix + ".active").
                isEqualTo(expectedTask.isActive());

        assertThat(actualTask.getParameters()).as(prefix + "parameters.size").
                hasSize(expectedTask.getParameters().size());

        for (int i=0; i<expectedTask.getParameters().size(); i++) {
            final Parameter expectedParameter = expectedTask.getParameters().get(i);
            final Parameter actualParameter = actualTask.getParameters().get(i);
            assertThat(expectedParameter.getName()).as(prefix + ".parameter[" + i + "].name").
                    isEqualTo(actualParameter.getName());
            assertThat(expectedParameter.getValue()).as(prefix + ".parameter[" + i + "].value").
                    isEqualTo(actualParameter.getValue());
        }

        assertThat(actualTask.getChildren()).hasSize(expectedTask.getChildren().size());
        for (int i=0; i<expectedTask.getChildren().size(); i++) {
            final Task expectedChild = expectedTask.getChildren().get(i);
            final Task actualChild = actualTask.getChildren().get(i);
            assertTask(expectedChild, actualChild, prefix + ".children[" + i + "]");
        }

    }

    public static void assertTask(Task expectedTask, Task actualTask) {
        assertTask(expectedTask, actualTask, "task");
    }
}
