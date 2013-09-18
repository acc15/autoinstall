package ru.vmsoftware.autoinstall.core.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public enum ActionType {

    NULL("null") {
        @Override
        public Action getAction() {
            return NullAction.getInstance();
        }
    },
    COPY("copy") {
        @Override
        public Action getAction() {
            return new CopyAction();
        }
    },
    EXECUTE("execute") {
        @Override
        public Action getAction() {
            return new ExecuteAction();
        }
    },
    REGISTRY("registry") {
        @Override
        public Action getAction() {
            return new RegistryAction();
        }
    };

    private static final HashMap<String, ActionType> DEFINITION_MAP = new HashMap<>();

    static {
        for (final ActionType def: values()) {
            DEFINITION_MAP.put(def.getName(), def);
        }
    }

    public static ActionType getDefinitionByName(String name) {
        return DEFINITION_MAP.get(name);
    }

    public static List<ActionType> getAvailableActions() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }

    private String name;

    ActionType(String name) {
        this.name = name;
    }

    public abstract Action getAction();

    public String getName() {
        return name;
    }


}
