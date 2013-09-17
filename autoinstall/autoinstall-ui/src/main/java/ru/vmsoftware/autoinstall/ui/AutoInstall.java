package ru.vmsoftware.autoinstall.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-16-09
 */
public class AutoInstall extends Application {

    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 300;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(AutoInstall.class.getName());
        final FXMLLoader loader = new FXMLLoader(
                AutoInstall.class.getResource("autoinstall.fxml"),
                resourceBundle);

        final Parent root = (Parent)loader.load();
        final Scene scene = new Scene(root);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle(resourceBundle.getString("key.title"));
        stage.setScene(scene);
        List<String> a = scene.getStylesheets();

        stage.getIcons().add(new Image(AutoInstall.class.getResource("autoinstall.png").toString()));
        stage.show();
    }
}
