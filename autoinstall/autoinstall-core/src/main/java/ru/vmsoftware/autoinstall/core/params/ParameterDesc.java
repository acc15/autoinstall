package ru.vmsoftware.autoinstall.core.params;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.task.TaskException;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class ParameterDesc<T> {

    private T defaultValue;
    private String name;
    private Class<T> type;

    /**
     * Initializes mandatory parameter description
     * @param name name of parameter
     * @param type type of parameter
     */
    public ParameterDesc(String name, Class<T> type) {
        this(name, type, null);
    }

    /**
     * Initializes parameter description
     * @param name name of parameter
     * @param type type of parameter
     * @param defaultValue default value which should be returned if current parameter wasn't found.
     *                     Note that if this value is null than parameter
     *                     should be considered {@link #isMandatory() mandatory}
     *
     */
    public ParameterDesc(String name, Class<T> type, T defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Evaluates parameter described by this object using context
     * @param context evaluation context
     * @return {@link ExecutionContext#getParameter(String, Class) parameter} returned from context
     *      or {@link #getDefaultValue() default value} specified in this descriptor
     * @throws TaskException in case when mandatory parameter is missing or have illegal type
     * @see ExecutionContext#getParameter(String, Class)
     */
    public T getValue(ExecutionContext context) throws TaskException {
        final T value = context.getParameter(name, type);
        if (value != null) {
            return value;
        }
        if (defaultValue == null) {
            throw new TaskException("mandatory parameter \"" + name + "\" missing in context");
        }
        return defaultValue;
    }

    /**
     * Returns default value for current parameter
     * @return default value for current parameter
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns whether this parameter is mandatory or not
     * @return whether this parameter is mandatory or not
     */
    public boolean isMandatory() {
        return defaultValue == null;
    }

    /**
     * Returns name of parameter
     * @return name of parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Returns type of parameter
     * @return type of parameter
     */
    public Class<T> getType() {
        return type;
    }

}
