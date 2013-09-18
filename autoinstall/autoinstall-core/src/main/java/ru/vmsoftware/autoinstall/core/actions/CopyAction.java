package ru.vmsoftware.autoinstall.core.actions;

import ru.vmsoftware.autoinstall.core.ExecutionContext;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CopyAction implements Action {

    public static final ActionDefinition<CopyAction> DEFINITION = new ActionDefinition<CopyAction>() {
        @Override
        public String getName() {
            return "copy";
        }

        @Override
        public CopyAction getAction() {
            return new CopyAction();
        }
    };

    private static final ParameterDesc<File> SOURCE_PATH = new ParameterDesc<>("sourcePath", File.class);
    private static final ParameterDesc<String> TARGET_PATH = new ParameterDesc<>("targetPath", String.class);
    private static final ParameterDesc<CopyMode> COPY_MODE = new ParameterDesc<>(
            "copyMode", CopyMode.class, CopyMode.OVERWRITE_EXISTING);

    public static enum CopyMode {

        /**
         * Delete all existing files at target path and make a fresh copy of files references by source path
         */
        CLEAN_COPY,

        /**
         * Overwrites existing files at target path by files references by source path
         */
        OVERWRITE_EXISTING,

        /**
         * Same as {@link #OVERWRITE_EXISTING} but doesn't overwrite existing files
         */
        LEAVE_EXISTING

    }


    @Override
    public void execute(ExecutionContext context) throws TaskException{
        final File sourceFile = SOURCE_PATH.getValue(context);
        final String targetPath = TARGET_PATH.getValue(context);
        final CopyMode copyMode = COPY_MODE.getValue(context);

        // TODO implement..

    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Arrays.<ParameterDesc<?>>asList(SOURCE_PATH, TARGET_PATH, COPY_MODE);
    }


    @Override
    public ActionDefinition<?> getDefinition() {
        return DEFINITION;
    }
}
