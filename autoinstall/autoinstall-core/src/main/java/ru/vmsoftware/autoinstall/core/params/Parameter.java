package ru.vmsoftware.autoinstall.core.params;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class Parameter {

    private String name;
    private String value;

    public Parameter() {
    }

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
