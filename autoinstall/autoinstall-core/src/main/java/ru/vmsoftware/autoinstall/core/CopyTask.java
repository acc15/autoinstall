package ru.vmsoftware.autoinstall.core;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class CopyTask implements Task {

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
    public void execute(ExecutionContext context) {
        // TODO implement..

    }

    @Override
    public List<Task> getChildren() {
        return null;
    }

    @Override
    public List<ParameterDesc<?>> getParameterDefinitions() {
        return Arrays.<ParameterDesc<?>>asList(SOURCE_PATH, TARGET_PATH, COPY_MODE);
    }

    @Override
    public List<Parameter<?>> getParameters() {
        // TODO implement..
        return null;
    }
}
