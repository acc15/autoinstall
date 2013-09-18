package ru.vmsoftware.autoinstall.core.actions;

import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public interface ActionRegistry {

    List<ActionDefinition<?>> getAvailableActions();

}