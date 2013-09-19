package ru.vmsoftware.autoinstall.ui;

import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class JavaFXUtils {

    public static void centerRelativeToOwner(Stage stage) {
        final Window owner = stage.getOwner();
        stage.setX(owner.getX() + owner.getWidth()/2 - stage.getWidth()/2);
        stage.setY(owner.getY() + owner.getHeight()/2 - stage.getHeight()/2);
    }

}
