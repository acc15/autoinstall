package ru.vmsoftware.javafx.dialogs;

/**
 * Enum which contains all packaged icons which can be set for dialog
 * @author Vyacheslav Mayorov
 * @since 2013-01-10
 */
public enum DialogIcon {

    /**
     * Exclamation mark on yellow background
     */
    WARNING("warning.png"),

    /**
     * Exclamation mark on blue background
     */
    NOTIFICATION("notification.png"),

    /**
     * Exclamation mark on red background
     */
    ERROR("error.png");

    private String path;

    DialogIcon(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
