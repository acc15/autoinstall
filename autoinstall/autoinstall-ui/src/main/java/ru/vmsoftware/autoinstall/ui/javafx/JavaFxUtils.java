package ru.vmsoftware.autoinstall.ui.javafx;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import ru.vmsoftware.autoinstall.ui.model.TaskItemModel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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

    public static void fireTreeItemChangeEvent(TreeItem<TaskItemModel> item) {
        Event.fireEvent(item, new TreeItem.TreeModificationEvent<>(
                TreeItem.valueChangedEvent(), item, item.getValue()));
    }

    public static String getStringOrDefault(ResourceBundle resourceBundle, String key, String defaultValue) {
        return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : defaultValue;
    }

    public static <T> T getValueOrDefault(ResourceBundle resourceBundle, String key, T defaultValue) {
        if (!resourceBundle.containsKey(key)) {
            return defaultValue;
        }
        final String stringValue = resourceBundle.getString(key);
        final T value;
        try {
            value = (T)defaultValue.getClass().getMethod("valueOf", String.class).invoke(stringValue);
        } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new RuntimeException("unable to invoke \"valueOf\" method for class " + defaultValue.getClass(), e);
        }
        return value;
    }

    public static Stage createStage(final StageController stageController) {

        final Class<?> clazz = stageController.getClass();
        final String className = clazz.getName();
        final String simpleClassName = clazz.getSimpleName();

        final Stage stage = new Stage();
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(className);

        if (!getValueOrDefault(resourceBundle, "fxml.disable", false)) {
            final String fxmlPath = getStringOrDefault(resourceBundle, "fxml.path", simpleClassName + ".fxml");
            final URL fxmlUrl = clazz.getResource(fxmlPath);
            final FXMLLoader loader = new FXMLLoader(fxmlUrl, resourceBundle);
            final Callback<Class<?>, Object> initialFactory = loader.getControllerFactory();

            // I need to keep reference to controller in FXML to make SceneBuilder working
            // This is because i can't simple setController
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> aClass) {
                    if (aClass.equals(clazz)) {
                        return stageController;
                    } else {
                        return initialFactory.call(aClass);
                    }
                }
            });
            try {
                stage.setScene(new Scene((Parent)loader.load()));
            } catch (IOException e) {
                throw new RuntimeException("unable to load scene from url: " + fxmlUrl);
            }
        }

        final URL iconUrl = clazz.getResource(getStringOrDefault(resourceBundle, "fxml.icon", simpleClassName));
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }
        stage.setTitle(getStringOrDefault(resourceBundle, "fxml.title", null));

        stageController.initialize(stage, resourceBundle);
        return stage;
    }
}
