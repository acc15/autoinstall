package ru.vmsoftware.autoinstall.ui.javafx;

import javafx.util.StringConverter;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public abstract class StringConverterAdapter<T> extends StringConverter<T> {
    @Override
    public T fromString(String s) {
        return null;
    }
}
