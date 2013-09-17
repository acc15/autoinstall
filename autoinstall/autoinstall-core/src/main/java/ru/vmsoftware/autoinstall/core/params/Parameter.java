package ru.vmsoftware.autoinstall.core.params;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class Parameter<T> {

    private String name;
    private Class<T> type;
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
