package ru.vmsoftware.autoinstall.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.serialization.SerializationUtils;
import ru.vmsoftware.autoinstall.core.serialization.SerializerFactory;
import ru.vmsoftware.autoinstall.core.serialization.TaskSerializerFactory;
import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.ui.dialog.UnsavedChangesDialog;
import ru.vmsoftware.autoinstall.ui.dialog.YesNoCancelEnum;
import ru.vmsoftware.autoinstall.ui.javafx.CheckedBoxTreeCellFactory;
import ru.vmsoftware.autoinstall.ui.javafx.JavaFxUtils;
import ru.vmsoftware.autoinstall.ui.javafx.SelectedTreeItemPropertyBinder;
import ru.vmsoftware.autoinstall.ui.javafx.StringConverterAdapter;
import ru.vmsoftware.autoinstall.ui.model.DocumentViewModel;
import ru.vmsoftware.autoinstall.ui.model.TaskItemModel;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class AutoInstallController implements Initializable {

    private static final String TASK_INITIAL_DESCRIPTION = "task.initialDescription";
    private static final String TASK_ROOT_INITIAL_DESCRIPTION = "task.rootInitialDescription";

    @FXML
    private TreeView<TaskItemModel> taskList;

    @FXML
    private ComboBox<ActionType> taskActionComboBox;

    @FXML
    private TextField taskDescriptionTextField;

    @FXML
    private TextArea taskConditionsTextArea;

    @FXML
    private MenuItem addTaskMenuItem;

    @FXML
    private MenuItem deleteTaskMenuItem;

    @FXML
    private ImageView addTaskIcon;

    @FXML
    private ImageView deleteTaskIcon;

    private ResourceBundle resourceBundle;

    private Map<ActionType,Image> cachedIcons = new WeakHashMap<>();

    private DocumentViewModel document;

    private SerializerFactory<Task> serializerFactory = new TaskSerializerFactory();

    private Stage stage;

    private <T> void bindToSelectedItemProperty(Property<T> property, String propertyName) {
        property.addListener(new SelectedTreeItemPropertyBinder<>(taskList, propertyName));
    }

    @FXML
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;

        taskList.setCellFactory(new CheckedBoxTreeCellFactory<>(new StringConverterAdapter<TreeItem<TaskItemModel>>() {
            @Override
            public String toString(TreeItem<TaskItemModel> taskTreeItem) {
                return taskTreeItem.getValue().getDescription();
            }
        }));
        taskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TaskItemModel>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<TaskItemModel>> observableValue,
                                TreeItem<TaskItemModel> oldItem,
                                TreeItem<TaskItemModel> newItem) {
                updateSceneForSelectedTreeItem(newItem);
            }
        });

        taskList.setRoot(createTaskItem(TASK_ROOT_INITIAL_DESCRIPTION));

        taskActionComboBox.setItems(new ObservableListWrapper<>(ActionType.getAvailableActions()));
        taskActionComboBox.setConverter(new StringConverterAdapter<ActionType>() {
            @Override
            public String toString(ActionType actionType) {
                return resourceBundle.getString("task." + actionType.getName());
            }
        });

        bindToSelectedItemProperty(taskActionComboBox.valueProperty(), "actionType");
        bindToSelectedItemProperty(taskDescriptionTextField.textProperty(), "description");
        bindToSelectedItemProperty(taskConditionsTextArea.textProperty(), "conditions");
        updateSceneForSelectedTreeItem(null);

    }

    @FXML
    public void newInstallation() {
        if (!checkForUnsavedChanges()) {
            return;
        }
        taskList.setRoot(createTaskItem(TASK_ROOT_INITIAL_DESCRIPTION));
        document.markNew(resourceBundle.getString(DocumentViewModel.KEY_NEW_NAME));
    }

    @FXML
    public void open() {


    }

    @FXML
    public void save() {

    }

    @FXML
    public void saveAs() {

    }

    @FXML
    public void close() {
        if (!checkForUnsavedChanges()) {
            return;
        }
        Platform.exit();
    }

    @FXML
    public void addTaskClick() {
        final TreeItem<TaskItemModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final CheckBoxTreeItem<TaskItemModel> newItem = createTaskItem(TASK_INITIAL_DESCRIPTION);
        selectedItem.getChildren().add(newItem);
        taskList.getSelectionModel().select(newItem);
    }

    @FXML
    public void deleteTaskClick() {
        final TreeItem<TaskItemModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final TreeItem<TaskItemModel> parentItem = selectedItem.getParent();
        final int itemPosition = parentItem.getChildren().indexOf(selectedItem);
        if (itemPosition < 0) {
            throw new IllegalArgumentException("can't find selectedItem in parentItem");
        }

        int insertionPos = itemPosition;
        final Iterator<TreeItem<TaskItemModel>> iter = selectedItem.getChildren().iterator();
        while (iter.hasNext()) {
            final TreeItem<TaskItemModel> child = iter.next();
            iter.remove();
            parentItem.getChildren().add(++insertionPos, child);
        }
        parentItem.getChildren().remove(itemPosition);
        taskList.getSelectionModel().select(parentItem);

    }

    @FXML
    public void installClick() {
        System.out.println("install");
    }

    public void setDocument(DocumentViewModel document) {
        this.document = document;
    }

    public DocumentViewModel getDocument() {
        return document;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    private File showSaveDialog() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("key.save"));
        fileChooser.setInitialFileName(document.getDocumentPath());
        fileChooser.getExtensionFilters().
                addAll(JavaFxUtils.buildExtensionFilters(resourceBundle, serializerFactory.getSupportedExtensions()));
        return fileChooser.showSaveDialog(stage);
    }

    private boolean checkForUnsavedChanges() {
        if (!document.isModified()) {
            return true;
        }

        final YesNoCancelEnum userChoice = UnsavedChangesDialog.showDialog(stage);
        switch (userChoice) {
            case YES:
                final File file = showSaveDialog();
                if (file == null) {
                    return false;
                }

                final TaskViewModel taskViewModel = (TaskViewModel) taskList.getRoot();
                final Task domainModel = ViewUtils.mapViewToDomain(taskViewModel);
                try {
                    SerializationUtils.serializeToFile(serializerFactory, file, domainModel);
                } catch (IOException e) {
                    // TODO show error dialog
                }
                break;

            case CANCEL:
                return false;
        }
        return true;
    }

    private ImageView getIconByActionType(ActionType actionType) {
        Image icon = cachedIcons.get(actionType);
        if (icon == null) {
            icon = new Image(
                    AutoInstallController.class.getResource(actionType.getName() + "-action.png").toExternalForm());
            cachedIcons.put(actionType, icon);
        }
        return new ImageView(icon);
    }

    private TaskViewModel createTaskItem(String initialDescriptionKey) {
        final TaskViewModel taskItem = new TaskViewModel();
        final TaskItemModel task = taskItem.getValue();
        task.descriptionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                JavaFxUtils.fireTreeItemChangeEvent(taskItem);
            }
        });
        task.actionTypeProperty().addListener(new ChangeListener<ActionType>() {
            @Override
            public void changed(ObservableValue<? extends ActionType> o, ActionType old, ActionType actionType) {
                taskItem.setGraphic(getIconByActionType(actionType));
                updateTaskManagementItemsForSelectedTreeItem(taskItem);
                JavaFxUtils.fireTreeItemChangeEvent(taskItem);
            }
        });

        task.setDescription(resourceBundle.getString(initialDescriptionKey));
        task.setActionType(ActionType.NULL);
        return taskItem;
    }

    private void setDisabledForTaskManagementItems(boolean disableAdd, boolean disableDelete) {
        JavaFxUtils.setDisabledWithEffect(addTaskIcon, disableAdd);
        JavaFxUtils.setDisabledWithEffect(deleteTaskIcon, disableDelete);
        addTaskMenuItem.setDisable(disableAdd);
        deleteTaskMenuItem.setDisable(disableDelete);
    }

    private void updateTaskManagementItemsForSelectedTreeItem(TreeItem<TaskItemModel> selectedItem) {
        setDisabledForTaskManagementItems(
                selectedItem == null || selectedItem.getValue().getActionType() != ActionType.NULL,
                selectedItem == null || selectedItem.getParent() == null);
    }

    private void setupTaskControls(boolean disable, boolean disableType, TaskItemModel task) {
        taskDescriptionTextField.setDisable(disable);
        taskConditionsTextArea.setDisable(disable);
        taskActionComboBox.setDisable(disable || disableType);
        taskDescriptionTextField.setText(task != null ? task.getDescription() : null);
        taskConditionsTextArea.setText(task != null ? task.getConditions() : null);
        taskActionComboBox.setValue(task != null ? task.getActionType() : null);
    }

    private void updateSceneForSelectedTreeItem(TreeItem<TaskItemModel> selectedItem) {
        updateTaskManagementItemsForSelectedTreeItem(selectedItem);
        setupTaskControls(selectedItem == null,
                selectedItem == null || !selectedItem.getChildren().isEmpty(),
                selectedItem != null ? selectedItem.getValue() : null);
    }

}
