package ru.vmsoftware.autoinstall.ui.javafx;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class JavaFxUtils {

    private static final Effect DISABLED_EFFECT = new ColorAdjust(0, -1, 0, 0);

    public static void centerRelativeToOwner(Stage stage) {
        final Window owner = stage.getOwner();
        stage.setX(owner.getX() + owner.getWidth()/2 - stage.getWidth()/2);
        stage.setY(owner.getY() + owner.getHeight()/2 - stage.getHeight()/2);
    }

    public static void setDisabledWithEffect(Node view, boolean disable) {
        view.setDisable(disable);
        view.setEffect(disable ? DISABLED_EFFECT : null);
    }

    public static List<FileChooser.ExtensionFilter> buildExtensionFilters(
            ResourceBundle resourceBundle,
            List<String> extensions) {
        final List<FileChooser.ExtensionFilter> filters = new ArrayList<>();
        for (final String ext: extensions) {
            filters.add(new FileChooser.ExtensionFilter(resourceBundle.getString("ext." + ext), "*." + ext));
        }
        filters.add(new FileChooser.ExtensionFilter(resourceBundle.getString("ext.all"), "*.*"));
        return filters;
    }

    public static void fireTreeItemChangeEvent(TreeItem<TaskViewModel> item) {
        Event.fireEvent(item, new TreeItem.TreeModificationEvent<>(
                TreeItem.valueChangedEvent(), item, item.getValue()));
    }
}
