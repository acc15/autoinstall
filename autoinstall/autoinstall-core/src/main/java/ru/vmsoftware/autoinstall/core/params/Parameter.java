package ru.vmsoftware.autoinstall.core.params;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class Parameter<T> {

    private String name;
    private T value;

    public Parameter() {
    }

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, T value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
