package ru.vmsoftware.autoinstall.core.task;

/**
 * Checked exception which is raised in cases when task
 * {@link Task#execute(ru.vmsoftware.autoinstall.core.ExecutionContext) execute}
 * can't be performed due to some error
 *
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class TaskException extends Exception {

    /**
     * Initializes exception with message and cause
     * @param message text description of error
     * @param cause cause of error
     */
    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Initializes exception with cause
     * @param cause cause of error
     */
    public TaskException(Throwable cause) {
        super(cause);
    }

    /**
     * Initializes exception with message
     * @param message text description of error
     */
    public TaskException(String message) {
        super(message);
    }
}
