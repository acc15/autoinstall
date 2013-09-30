package ru.vmsoftware.autoinstall.ui.dialog;

import javafx.stage.Stage;
import javafx.stage.Window;
import ru.vmsoftware.autoinstall.ui.javafx.JavaFxUtils;
import ru.vmsoftware.autoinstall.ui.javafx.StageController;

import java.util.ResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class UnsavedChangesDialog implements StageController {

    public static YesNoCancelEnum showDialog(Window owner) {
        final UnsavedChangesDialog controller = new UnsavedChangesDialog();
        JavaFxUtils.createStage(controller, owner).showAndWait();
        return controller.whatWasChosen;
    }

    @Override
    public void initialize(Stage stage, ResourceBundle resourceBundle) {
        this.stage = stage;
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
