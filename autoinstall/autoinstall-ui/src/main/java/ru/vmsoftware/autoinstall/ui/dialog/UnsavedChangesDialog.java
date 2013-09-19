package ru.vmsoftware.autoinstall.ui.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.vmsoftware.autoinstall.ui.JavaFXUtils;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class UnsavedChangesDialog {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 100;

    public static YesNoCancelEnum showDialog(Window owner) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(UnsavedChangesDialog.class.getName());

        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        JavaFXUtils.centerRelativeToOwner(stage);
        stage.setTitle(resourceBundle.getString("key.title"));

        final FXMLLoader loader = new FXMLLoader(
                UnsavedChangesDialog.class.getResource("UnsavedChangesDialog.fxml"),
                resourceBundle);
        final Parent root;
        try {
            root = (Parent)loader.load();
        } catch (IOException e) {
            throw new RuntimeException("unable to load UnsavedChangesDialog");
        }

        final UnsavedChangesDialog controller = loader.getController();
        controller.stage = stage;

        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        return controller.whatWasChosen;
    }

    private Stage stage;
    private YesNoCancelEnum whatWasChosen = YesNoCancelEnum.CANCEL;

    public void save() {
        whatWasChosen = YesNoCancelEnum.YES;
        stage.close();
    }

    public void dontSave() {
        whatWasChosen = YesNoCancelEnum.NO;
        stage.close();
    }

    public void cancel() {
        whatWasChosen = YesNoCancelEnum.CANCEL;
        stage.close();
    }

}
