package ru.vmsoftware.autoinstall.core;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public interface Condition {

    boolean evaluate(ExecutionContext context);

}
