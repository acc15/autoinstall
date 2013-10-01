package ru.vmsoftware.autoinstall.ui.javafx;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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

    public static double center(double offset, double size1, double size2) {
        return offset + size1/2 - size2/2;
    }

    public static void centerRelativeTo(Stage stage, Rectangle2D bounds) {
        stage.setX(center(bounds.getMinX(), bounds.getWidth(), stage.getWidth()));
        stage.setY(center(bounds.getMinY(), bounds.getHeight(), stage.getHeight()));
    }

    public static Rectangle2D getScreenBounds() {
        return Screen.getPrimary().getBounds();
    }

    public static Rectangle2D getWindowBounds(Window window) {
        return new Rectangle2D(window.getX(),
                window.getY(),
                window.getWidth(),
                window.getHeight());
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

    @SuppressWarnings("unchecked")
    public static <T> T getValueOrDefault(ResourceBundle resourceBundle, String key, T defaultValue) {
        if (!resourceBundle.containsKey(key)) {
            return defaultValue;
        }
        final String stringValue = resourceBundle.getString(key);
        final T value;
        try {
            value = (T)defaultValue.getClass().getMethod("valueOf", String.class).invoke(null, stringValue);
        } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new RuntimeException("unable to invoke \"valueOf\" method for class " + defaultValue.getClass(), e);
        }
        return value;
    }

    public static Stage createStage(final StageController stageController) {
        return createStage(stageController, null);
    }

    private static double doubleOrDefault(double v, double def) {
        return Double.isInfinite(v) || Double.isNaN(v) ? def : v;
    }


    public static Stage createStage(final StageController stageController, final Window owner) {

        final String stagePrefix = "stage.";
        final Class<?> clazz = stageController.getClass();
        final String className = clazz.getName();
        final String simpleClassName = clazz.getSimpleName();

        final Stage stage = new Stage();
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(className);

        if (!getValueOrDefault(resourceBundle, stagePrefix + "disable", false)) {
            final String fxmlPath = getStringOrDefault(resourceBundle, stagePrefix + "fxml", simpleClassName + ".fxml");
            final URL fxmlUrl = clazz.getResource(fxmlPath);
            final FXMLLoader loader = new FXMLLoader(fxmlUrl, resourceBundle);
            final Callback<Class<?>, Object> initialFactory = loader.getControllerFactory();
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

            final Region parent;
            try {
                parent = (Region) loader.load();
            } catch (IOException e) {
                throw new RuntimeException("unable to load scene from url: " + fxmlUrl);
            }

            stage.setWidth(doubleOrDefault(parent.getPrefWidth(), stage.getWidth()));
            stage.setHeight(doubleOrDefault(parent.getPrefHeight(), stage.getHeight()));
            stage.setMinWidth(doubleOrDefault(parent.getMinWidth(), stage.getWidth()));
            stage.setMinHeight(doubleOrDefault(parent.getMinHeight(), stage.getHeight()));
            stage.setMaxWidth(doubleOrDefault(parent.getMaxWidth(), stage.getMaxWidth()));
            stage.setMaxHeight(doubleOrDefault(parent.getMaxHeight(), stage.getMaxHeight()));

            stage.setScene(new Scene(parent));
        }

        stage.initOwner(owner);
        stage.initStyle(getValueOrDefault(resourceBundle, stagePrefix + "style", stage.getStyle()));
        stage.initModality(getValueOrDefault(resourceBundle, stagePrefix + "modality", stage.getModality()));
        stage.setResizable(getValueOrDefault(resourceBundle, stagePrefix + "resizable", stage.isResizable()));
        stage.setMinWidth(getValueOrDefault(resourceBundle, stagePrefix + "minWidth", stage.getMinWidth()));
        stage.setMinHeight(getValueOrDefault(resourceBundle, stagePrefix + "minHeight", stage.getMinHeight()));
        stage.setMaxWidth(getValueOrDefault(resourceBundle, stagePrefix + "maxWidth", stage.getMaxWidth()));
        stage.setMaxHeight(getValueOrDefault(resourceBundle, stagePrefix + "maxHeight", stage.getMaxHeight()));
        stage.setWidth(getValueOrDefault(resourceBundle, stagePrefix + "width", stage.getWidth()));
        stage.setHeight(getValueOrDefault(resourceBundle, stagePrefix + "height", stage.getHeight()));

        if (getValueOrDefault(resourceBundle, stagePrefix + "centerOwner", true)) {
            final Rectangle2D rectangle2D = owner != null ? getWindowBounds(owner) : getScreenBounds();
            JavaFxUtils.centerRelativeTo(stage, rectangle2D);
        }
        stage.setTitle(getStringOrDefault(resourceBundle, stagePrefix + "title", null));

        final URL iconUrl = clazz.getResource(getStringOrDefault(resourceBundle, stagePrefix + "icon", simpleClassName + ".png"));
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }

        stageController.initialize(stage, resourceBundle);
        return stage;
    }
}
