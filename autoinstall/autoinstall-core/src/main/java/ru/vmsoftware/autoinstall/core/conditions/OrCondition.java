package ru.vmsoftware.autoinstall.core.conditions;

import ru.vmsoftware.autoinstall.core.ExecutionContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class OrCondition extends Condition {
    @Override
    public boolean evaluate(ExecutionContext context) {
        for (final Condition child: getChildren()) {
            if (child.evaluate(context)) {
                return true;
            }
        }
        return false;
    }
}
