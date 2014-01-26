package ru.vmsoftware.javafx.dialogs.bundle;

import ru.vmsoftware.javafx.dialogs.DialogTest;

import java.util.ListResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-03-10
 */
public class TestBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new String[][] {
                {"custom.title", DialogTest.TEST_TITLE},
                {"custom.icon", DialogTest.TEST_ICON.toString()},
                {"custom.message", DialogTest.TEST_MESSAGE},
                {"custom.details", DialogTest.TEST_DETAILS_LABEL},
                {"custom.OK", DialogTest.TEST_DEFAULT_BUTTON},
                {"custom.CANCEL", DialogTest.TEST_CANCEL_BUTTON},
                {"custom.CLOSED", DialogTest.TEST_BUTTON}
        };
    }
}
