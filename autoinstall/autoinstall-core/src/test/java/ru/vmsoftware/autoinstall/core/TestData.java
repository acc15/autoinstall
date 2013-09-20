package ru.vmsoftware.autoinstall.core;

import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.task.Task;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class TestData {


    public static Task createSampleTask() {
        final Task expectedTask = new Task();
        expectedTask.setDescription("String");
        expectedTask.setConditions("String");

        final Parameter p1 = new Parameter("abc", "hjuhukio");
        expectedTask.getParameters().add(p1);

        final Task c1 = new Task();
        c1.setActive(false);
        c1.setDescription("test task");
        c1.setConditions("a == b");
        expectedTask.getChildren().add(c1);

        final Task c11 = new Task();
        c11.setActionType(ActionType.EXECUTE);

        final Parameter p11 = new Parameter("abc", "hjuhukio");
        c11.getParameters().add(p11);
        c1.getChildren().add(c11);
        return expectedTask;
    }
}
