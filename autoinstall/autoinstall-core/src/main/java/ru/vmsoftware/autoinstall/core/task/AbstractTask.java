package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public abstract class AbstractTask implements Task {

    private String description = "";
    private List<Parameter<?>> parameters = new ArrayList<>();

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        if (description == null) {
            throw new NullPointerException("task description can't be null");
        }
        this.description = description;
    }

    @Override
    public final List<Parameter<?>> getParameters() {
        return parameters;
    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Collections.emptyList();
    }
}
