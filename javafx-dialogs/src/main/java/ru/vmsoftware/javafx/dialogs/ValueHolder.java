package ru.vmsoftware.javafx.dialogs;

/**
 * Simple holder for storing {@link Dialog} result value
 * @author Vyacheslav Mayorov
 * @since 2013-01-10
 */
public class ValueHolder<T> {

    /**
     * Returns stored value
     * @return stored value
     */
    public T getValue() {
        return value;
    }

    /**
     * Stores new value
     * @param value value to set
     */
    public void setValue(T value) {
        this.value = value;
    }

    private T value;
}
