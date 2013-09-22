package ru.vmsoftware.autoinstall.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.vmsoftware.autoinstall.ui.javafx.JavaFxUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-16-09
 */
public class AutoInstall extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        JavaFxUtils.createStage(new AutoInstallMain()).show();
    }
}
