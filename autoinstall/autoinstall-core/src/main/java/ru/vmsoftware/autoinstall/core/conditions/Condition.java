package ru.vmsoftware.autoinstall.core.conditions;

import ru.vmsoftware.autoinstall.core.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public abstract class Condition {

    public abstract boolean evaluate(ExecutionContext context);

    public List<Condition> getChildren() {
        return children;
    }

    private List<Condition> children = new ArrayList<>();
}
