package ru.vmsoftware.javafx.dialogs.bundle;

import java.util.ListResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-03-10
 */
public class EmptyBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[0][];
    }
}
