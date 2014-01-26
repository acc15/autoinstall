package ru.vmsoftware.javafx.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Dialog for specifying options and building dialog stage
 * @author Vyacheslav Mayorov
 * @since 2013-01-10
 */
public class Dialog<T> {

    private static final String MESSAGE_KEY = "message";
    private static final String ICON_KEY = "icon";
    private static final String TITLE_KEY = "title";

    private static final int DEFAULT_SPACING = 10;
    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 100;
    private static final int MIN_BUTTON_WIDTH = 80;
    private static final int DETAILS_HEIGHT = 200;
    private static final String DETAILS_KEY = "details";

    private T defaultValue = null;
    private String title = null;
    private String message = null;
    private String detailsTitle = null;
    private String details = null;
    private List<DialogButton<T>> buttons = new ArrayList<>();
    private DialogIcon icon = null;
    private ResourceBundle resourceBundle = null;
    private String resourcePrefix = null;

    private Set<DialogButtonType> definedButtonTypes = new HashSet<>();

    private Dialog(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * <p>Sets dialog title</p>
     * <p>If dialog title won't be specified then it will be looked up
     * in {@link ResourceBundle} which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Title in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + "title"=$yourCustomTitle}</p>
     * <p>Examples: <ul>
     *  <li>{@code mydialog.title=Ooops... something goes wrong}</li>
     *  <li>{@code mydialog.title=Let's dance}</li>
     * </ul></p>
     * <p>In the case when neither title was specified, nor it found in {@link ResourceBundle}
     *    then empty title will be used</p>
     * @param title dialog title
     * @return {@code this} to allow method chaining
     * @see #resources(java.util.ResourceBundle, String)
     * @see #resources(java.util.ResourceBundle)
     */
    public Dialog<T> title(String title) {
        this.title = title;
        return this;
    }

    /**
     * <p>Sets dialog icon</p>
     * <p>If dialog icon won't be specified then it will be looked up
     * in {@link ResourceBundle} which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Icon in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + "icon"=}{@link DialogIcon one of DialogIcon value}.</p>
     * <p>Examples: <ul>
     *  <li>{@code mydialog.icon=ERROR}</li>
     *  <li>{@code mydialog.icon=WARNING}</li>
     *  <li>{@code mydialog.icon=NOTIFICATION}</li>
     * </ul></p>
     * <p>In the case when neither icon was specified, nor it found in {@link ResourceBundle}
     *    then no icon will appear</p>
     * @param icon dialog icon
     * @return {@code this} to allow method chaining
     * @see #resources(java.util.ResourceBundle, String)
     * @see #resources(java.util.ResourceBundle)
     */
    public Dialog<T> icon(DialogIcon icon) {
        this.icon = icon;
        return this;
    }

    /**
     * <p>Sets dialog message</p>
     * <p>If dialog text won't be specified then it will be looked up
     * in {@link ResourceBundle} which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Text in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + "message"=${yourMessage}</p>
     * <p>Examples: <ul>
     *  <li>{@code mydialog.message=Ok. What next?}</li>
     *  <li>{@code mydialog.message=Go back, human}</li>
     * </ul></p>
     * <p>In the case when neither message was specified, nor it found in {@link ResourceBundle}
     *    then message label will be absent</p>
     * @param message dialog message
     * @return {@code this} to allow method chaining
     */
    public Dialog<T> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * <p>Sets dialog details with label</p>
     * <p>If {@code details} won't be specified then no details pane will be shown.
     * However if you put {@code null} as {@code detailsLabel} parameter
     * this call will be equivalent to {@link #details(String)}</p>
     * @param detailsLabel label for {@link TitledPane}
     * @param details text of details (for example exception stack trace)
     * @return {@code this} to allow method chaining
     * @see #details(String)
     */
    public Dialog<T> details(String detailsLabel, String details) {
        this.detailsTitle = detailsLabel;
        this.details = details;
        return this;
    }

    /**
     * <p>Sets dialog details</p>
     * <p>If {@code details} won't be specified then no details pane will be shown</p>
     * <p>An label will be looked up in {@link ResourceBundle}
     * which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Text in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + "details"=${yourDetailsLabel}}</p>
     * <p>Examples: <ul>
     *  <li>{@code mydialog.details=Stacktrace}</li>
     *  <li>{@code mydialog.details=Details}</li>
     * </ul></p>
     * <p>When {@link #build(javafx.stage.Window, ValueHolder)} is called it performs further checks: <ul>
     *      <li>In the case when details label can't be found in {@link ResourceBundle} then
     *  {@link MissingResourceException} will be thrown</li>
     *      <li>If {@link ResourceBundle} wasn't specified then {@link IllegalStateException} will be thrown</li>
     *  </ul>
     * </p>
     * @param details details to show
     * @return {@code this} to allow method chaining
     *
     */
    public Dialog<T> details(String details) {
        this.details = details;
        return this;
    }

    /**
     * <p>Appends dialog button</p>
     * <p>An label will be looked up in {@link ResourceBundle}
     * which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Text in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + ${value.toString()}=${yourButtonLabel}}</p>
     * <p>For examples if you use {@link DialogResult}: <ul>
     *  <li>{@code mydialog.YES=Save changes}</li>
     *  <li>{@code mydialog.NO=Don't save}</li>
     *  <li>{@code mydialog.CANCEL=Cancel}</li>
     * </ul></p>
     * <p>When {@link #build(javafx.stage.Window, ValueHolder)} is called it performs further checks: <ul>
     *      <li>In the case when button label can't be found in {@link ResourceBundle} then
     *  {@link MissingResourceException} will be thrown</li>
     *      <li>If {@link ResourceBundle} wasn't specified then {@link IllegalStateException} will be thrown</li>
     *  </ul>
     * </p>
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     */
    public Dialog<T> button(T value) {
        return addButton(null, null, value);
    }

    /**
     * <p>Appends <i>cancel</i> dialog button. Cancel button can be only one. So if you try to add more then
     * one <i>cancel</i> button an {@link IllegalArgumentException} will be thrown</p>
     * <p>An label will be looked up in {@link ResourceBundle}
     * which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Text in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + ${value.toString()}=${yourButtonLabel}}</p>
     * <p>For examples if you use {@link DialogResult}: <ul>
     *  <li>{@code mydialog.YES=Save changes}</li>
     *  <li>{@code mydialog.NO=Don't save}</li>
     *  <li>{@code mydialog.CANCEL=Cancel}</li>
     * </ul></p>
     * <p>In the case when button label can't be found in {@link ResourceBundle}
     *    or {@link ResourceBundle} isn't specified
     *    then {@link IllegalStateException} will be thrown</p>
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     */
    public Dialog<T> cancelButton(T value) {
        return addButton(DialogButtonType.CANCEL, null, value);
    }

    /**
     * <p>Appends <i>default</i> dialog button. Default button can be only one. So if you try to add more then
     * one <i>default</i> button an {@link IllegalArgumentException} will be thrown</p>
     * <p>An label will be looked up in {@link ResourceBundle}
     * which can be specified by {@link #resources(java.util.ResourceBundle, String)}</p>
     * <p>Text in {@link ResourceBundle} should have following format:
     *  {@code ${resourcePrefix} + ${value.toString()}=${yourButtonLabel}}</p>
     * <p>For examples if you use {@link DialogResult}: <ul>
     *  <li>{@code mydialog.YES=Save changes}</li>
     *  <li>{@code mydialog.NO=Don't save}</li>
     *  <li>{@code mydialog.CANCEL=Cancel}</li>
     * </ul></p>
     * <p>In the case when button label can't be found in {@link ResourceBundle}
     *    or {@link ResourceBundle} isn't specified
     *    then {@link IllegalStateException} will be thrown</p>
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     */
    public Dialog<T> defaultButton(T value) {
        return addButton(DialogButtonType.DEFAULT, null, value);
    }

    /**
     * <p>Appends dialog button</p>
     * <p>If you put {@code null} as {@code label} parameter then
     *  this call will be equivalent to {@link #button(Object)}</p>
     * @param label button label
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     * @see #button(Object)
     */
    public Dialog<T> button(String label, T value) {
        return addButton(null, label, value);
    }

    /**
     * <p>Appends <i>cancel</i> dialog button. Cancel button can be only one. So if you try to add more then
     * one <i>cancel</i> button an {@link IllegalArgumentException} will be thrown</p>
     * <p>If you put {@code null} as {@code label} parameter then
     *  this call will be equivalent to {@link #button(Object)}</p>
     * @param label button label
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     * @see #button(Object)
     * @throws IllegalArgumentException
     */
    public Dialog<T> cancelButton(String label, T value) {
        return addButton(DialogButtonType.CANCEL, label, value);
    }

    /**
     * <p>Appends <i>default</i> dialog button. Default button can be only one. So if you try to add more then
     * one <i>default</i> button an {@link IllegalArgumentException} will be thrown</p>
     * <p>If you put {@code null} as {@code label} parameter then
     *  this call will be equivalent to {@link #button(Object)}</p>
     * @param label button label
     * @param value value to return when button will be clicked
     * @return {@code this} to allow method chaining
     * @see #button(Object)
     * @throws IllegalArgumentException
     */
    public Dialog<T> defaultButton(String label, T value) {
        return addButton(DialogButtonType.DEFAULT, label, value);
    }

    /**
     * <p>Specifies {@link ResourceBundle} to use for finding labels and other dialog settings.
     * All lookups will be performed with empty prefix</p>
     * <p>See another {@link Dialog} methods to get more information about entries</p>
     * @param resourceBundle resourceBundle to use for this dialog.
     * @return {@code this} to allow method chaining
     * @see #title(String)
     * @see #message(String)
     * @see #details(String)
     * @see #icon(DialogIcon)
     */
    public Dialog<T> resources(ResourceBundle resourceBundle) {
        return resources(resourceBundle, "");
    }

    /**
     * <p>Specifies {@link ResourceBundle} to use for finding labels and other dialog settings lookup.
     * All lookups will be performed with specified {@code prefix}</p>
     * <p>See another {@link Dialog} methods to get more information about entries</p>
     * @param resourceBundle resourceBundle to use for this dialog
     * @return {@code this} to allow method chaining
     * @see #title(String)
     * @see #message(String)
     * @see #details(String)
     * @see #icon(DialogIcon)
     */
    public Dialog<T> resources(ResourceBundle resourceBundle, String resourcePrefix) {
        this.resourceBundle = resourceBundle;
        this.resourcePrefix = resourcePrefix;
        return this;
    }

    /**
     * Builds dialog stage which can be reused many times
     * @param owner owner of dialog
     * @param resultHolder holder of dialog result
     * @return built {@link Stage}
     */
    public Stage build(Window owner, ValueHolder<T> resultHolder) {
        resultHolder.setValue(defaultValue);

        final Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);

        final String dialogTitle = title != null ? title : getResourceValueOrNull(TITLE_KEY);
        if (dialogTitle != null) {
            stage.setTitle(dialogTitle);
        }

        final List<Node> controls = new ArrayList<>();
        addHeaderBox(controls);
        addDetailsBox(controls, stage);
        addButtonBox(controls, stage, resultHolder);
        final VBox root = VBoxBuilder.create().
                fillWidth(true).
                spacing(DEFAULT_SPACING).
                padding(new Insets(DEFAULT_SPACING)).
                children(controls).build();

        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                final Window owner = stage.getOwner();
                final double centerX = owner.getX() + owner.getWidth()/2 - stage.getWidth()/2;
                final double centerY = owner.getY() + owner.getHeight()/2 - stage.getHeight()/2;
                stage.setX(centerX);
                stage.setY(centerY);
            }
        });
        return stage;
    }

    /**
     * Shows and wait until dialog will be closed
     * @param owner dialog owner
     * @return dialog result value
     */
    public T show(Window owner) {
        final ValueHolder<T> resultHolder = new ValueHolder<>();
        build(owner, resultHolder).showAndWait();
        return resultHolder.getValue();
    }

    /**
     * Creates dialog which will return given {@code defaultValue} if it will be closed
     * @param defaultValue default dialog result value
     * @param <T> type of dialog result
     * @return dialog for showing and specifying other options
     */
    public static <T> Dialog<T> withDefault(T defaultValue) {
        return new Dialog<>(defaultValue);
    }

    private static enum DialogButtonType {
        CANCEL,
        DEFAULT
    }

    private String getResourceKey(String key) {
        return resourcePrefix + key;
    }

    @SuppressWarnings("unchecked")
    private <T> T getResourceValueOrNull(String key, Class<T> clazz) {
        final String value = getResourceValueOrNull(key);
        try {
            return value != null
                    ? (T)clazz.getMethod("valueOf", String.class).invoke(null, value)
                    : null;
        } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
            throw new IllegalArgumentException("illegal resource value \"" + value + "\" for type " + clazz);
        }
    }

    private String getResourceValueOrNull(String key) {
        if (resourceBundle == null) {
            return null;
        }
        final String resourceKey = getResourceKey(key);
        return resourceBundle.containsKey(resourceKey) ? resourceBundle.getString(resourceKey) : null;
    }

    private String getResourceValue(String key) {

        final String resourceKey = getResourceKey(key);
        if (resourceBundle == null) {
            throw new IllegalStateException("resourceBundle wasn't specified but it's required to lookup value: " +
                    resourceKey);
        }
        return resourceBundle.getString(resourceKey);
    }

    private static class DialogButton<T> {
        DialogButtonType type;
        String label;
        T value;

        private DialogButton(DialogButtonType type, String label, T value) {
            this.type = type;
            this.label = label;
            this.value = value;
        }

        private DialogButtonType getType() {
            return type;
        }

        private String getLabel() {
            return label;
        }

        private T getValue() {
            return value;
        }
    }

    private Dialog<T> addButton(DialogButtonType type, String label, T value) {
        if (type != null && !definedButtonTypes.add(type)) {
            throw new IllegalStateException("button of type " + type + " already defined in dialog");
        }
        buttons.add(new DialogButton<>(type, label, value));
        return this;
    }

    private void addHeaderBox(List<Node> controls) {
        final List<Node> headerControls = new ArrayList<>();
        final DialogIcon dialogIcon = icon != null ? icon : getResourceValueOrNull(ICON_KEY, DialogIcon.class);
        if (dialogIcon != null) {
            headerControls.add(ImageViewBuilder.create().
                    image(new Image(Dialog.class.getResource(dialogIcon.getPath()).toExternalForm())).build());
        }

        final String dialogMessage = message != null ? message : getResourceValueOrNull(MESSAGE_KEY);
        if (dialogMessage != null) {
            headerControls.add(LabelBuilder.create().text(dialogMessage).build());
        }
        if (headerControls.isEmpty()) {
            return;
        }
        if (headerControls.size() == 1) {
            final Node control = headerControls.get(0);
            controls.add(control);
            return;
        }

        final HBox hBox = HBoxBuilder.create().spacing(DEFAULT_SPACING).
                alignment(Pos.CENTER_LEFT).children(headerControls).build();
        controls.add(hBox);
    }

    private void addDetailsBox(List<Node> controls, final Stage stage) {
        if (details == null) {
            return;
        }

        final String dialogDetailsTitle = detailsTitle != null ? detailsTitle : getResourceValue(DETAILS_KEY);
        final TitledPane pane = TitledPaneBuilder.create().
                expanded(false).
                collapsible(true).
                animated(false).
                maxHeight(Double.MAX_VALUE).
                text(dialogDetailsTitle).
                content(TextAreaBuilder.create().editable(false).wrapText(false).text(details).build()).
                build();
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    stage.setHeight(stage.getHeight() + DETAILS_HEIGHT);
                } else {
                    stage.setHeight(stage.getHeight() - DETAILS_HEIGHT);
                }
            }
        });
        controls.add(pane);
    }

    private void addButtonBox(List<Node> controls, final Stage stage, final ValueHolder<T> valueHolder) {
        if (buttons.isEmpty()) {
            return;
        }
        final List<Button> buttonControls = new ArrayList<>();
        for (final DialogButton<T> button: buttons) {
            final String dialogButtonLabel = button.getLabel() != null
                    ? button.getLabel()
                    : getResourceValue(button.getValue().toString());
            buttonControls.add(ButtonBuilder.create().
                    defaultButton(button.getType() == DialogButtonType.DEFAULT).
                    cancelButton(button.getType() == DialogButtonType.CANCEL).
                    minWidth(MIN_BUTTON_WIDTH).
                    text(dialogButtonLabel).onAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            valueHolder.setValue(button.getValue());
                            stage.close();
                        }
                    }).build());
        }

        final HBox hBox = HBoxBuilder.create().spacing(DEFAULT_SPACING).
                alignment(Pos.CENTER_RIGHT).
                children(buttonControls).build();
        controls.add(hBox);
    }

}
