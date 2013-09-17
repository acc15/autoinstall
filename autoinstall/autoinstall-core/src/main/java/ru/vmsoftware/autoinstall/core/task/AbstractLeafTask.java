package ru.vmsoftware.autoinstall.core.task;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public abstract class AbstractLeafTask extends AbstractTask {

    @Override
    public final List<Task> getChildren() {
        return null;
    }

}
