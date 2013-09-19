package ru.vmsoftware.autoinstall.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.ui.model.TaskViewModel;

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

    private static final Effect DISABLED_EFFECT = new ColorAdjust(0, -1, 0, 0);

    @FXML
    private TreeView<TaskViewModel> taskList;

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

    private ImageView getIconByActionType(ActionType actionType) {
        Image icon = cachedIcons.get(actionType);
        if (icon == null) {
            icon = new Image(
                    AutoInstallController.class.getResource(actionType.getName() + "-action.png").toExternalForm());
            cachedIcons.put(actionType, icon);
        }
        return new ImageView(icon);
    }

    private CheckBoxTreeItem<TaskViewModel> createTaskItem(String initialDescriptionKey) {
        final TaskViewModel task = new TaskViewModel();
        final CheckBoxTreeItem<TaskViewModel> item = new CheckBoxTreeItem<>(task);
        task.setDescription(resourceBundle.getString(initialDescriptionKey));
        item.setValue(task);
        item.setSelected(true);
        item.setExpanded(true);
        item.setIndependent(true);
        item.setGraphic(getIconByActionType(task.getActionType()));
        return item;
    }

    private void updateTaskManagementItemsForSelectedTreeItem(TreeItem<TaskViewModel> selectedItem) {

        if (selectedItem == null) {
            deleteTaskMenuItem.setDisable(true);
            addTaskMenuItem.setDisable(true);
            deleteTaskIcon.setDisable(true);
            addTaskIcon.setDisable(true);
            addTaskIcon.setEffect(DISABLED_EFFECT);
            deleteTaskIcon.setEffect(DISABLED_EFFECT);
            return;
        }

        final boolean disableAdd = selectedItem.getValue().getActionType() != ActionType.NULL;
        final boolean disableDelete = selectedItem.getParent() == null;

        addTaskIcon.setDisable(disableAdd);
        addTaskIcon.setEffect(disableAdd ? DISABLED_EFFECT : null);

        deleteTaskIcon.setDisable(disableDelete);
        deleteTaskIcon.setEffect(disableDelete ? DISABLED_EFFECT : null);

        addTaskMenuItem.setDisable(disableAdd);
        deleteTaskMenuItem.setDisable(disableDelete);
    }

    private void updateSceneForSelectedTreeItem(TreeItem<TaskViewModel> selectedItem) {

        updateTaskManagementItemsForSelectedTreeItem(selectedItem);

        if (selectedItem == null) {
            taskDescriptionTextField.setDisable(true);
            taskDescriptionTextField.setText(null);
            taskActionComboBox.setDisable(true);
            taskActionComboBox.setValue(null);
            taskConditionsTextArea.setDisable(true);
            taskConditionsTextArea.setText(null);
            return;
        }

        final TaskViewModel task = selectedItem.getValue();
        taskDescriptionTextField.setDisable(false);
        taskDescriptionTextField.setText(task.getDescription());
        taskConditionsTextArea.setDisable(false);
        taskConditionsTextArea.setText(task.getConditions());
        taskActionComboBox.setDisable(!selectedItem.getChildren().isEmpty());
        taskActionComboBox.setValue(task.getActionType());

    }

    private static void fireChangeEvent(TreeItem<TaskViewModel> selectedItem) {
        Event.fireEvent(selectedItem, new TreeItem.TreeModificationEvent<>(
                TreeItem.valueChangedEvent(), selectedItem, selectedItem.getValue()));
    }

    @FXML
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;

        taskList.setCellFactory(new CheckedBoxTreeCellFactory<>(new StringConverter<TreeItem<TaskViewModel>>() {
            @Override
            public String toString(TreeItem<TaskViewModel> taskTreeItem) {
                return taskTreeItem.getValue().getDescription();
            }

            @Override
            public TreeItem<TaskViewModel> fromString(String s) {
                return null;
            }
        }));
        taskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TaskViewModel>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<TaskViewModel>> observableValue,
                                TreeItem<TaskViewModel> oldItem,
                                TreeItem<TaskViewModel> newItem) {
                updateSceneForSelectedTreeItem(newItem);
            }
        });

        taskList.setRoot(createTaskItem("task.rootInitialDescription"));

        taskActionComboBox.setItems(new ObservableListWrapper<>(ActionType.getAvailableActions()));
        taskActionComboBox.setConverter(new StringConverter<ActionType>() {
            @Override
            public String toString(ActionType actionType) {
                return resourceBundle.getString("task." + actionType.getName());
            }

            @Override
            public ActionType fromString(String s) {
                return null;
            }
        });
        taskActionComboBox.valueProperty().addListener(new ChangeListener<ActionType>() {
            @Override
            public void changed(ObservableValue<? extends ActionType> observableValue,
                                ActionType oldDefinition,
                                ActionType newDefinition) {
                final TreeItem<TaskViewModel> item = taskList.getSelectionModel().getSelectedItem();
                if (item == null && newDefinition != null) {
                    throw new IllegalStateException("no item is selected in TreeView but task type was modified");
                } else if (item != null && newDefinition == null) {
                    throw new IllegalStateException("item is selected but null definition was selected");
                }

                if (newDefinition == null) {
                    return;
                }

                item.getValue().setActionType(newDefinition);
                item.setGraphic(getIconByActionType(item.getValue().getActionType()));

                updateTaskManagementItemsForSelectedTreeItem(item);
                fireChangeEvent(item);

            }
        });

        taskDescriptionTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                final TreeItem<TaskViewModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return;
                }
                selectedItem.getValue().setDescription(newValue);
                fireChangeEvent(selectedItem);
            }
        });

        taskConditionsTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                final TreeItem<TaskViewModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return;
                }
                selectedItem.getValue().setConditions(newValue);
            }
        });

        updateSceneForSelectedTreeItem(null);

    }

    @FXML
    public void addTaskClick() {
        final TreeItem<TaskViewModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final CheckBoxTreeItem<TaskViewModel> newItem = createTaskItem("task.initialDescription");
        selectedItem.getChildren().add(newItem);
        taskList.getSelectionModel().select(newItem);
    }

    @FXML
    public void deleteTaskClick() {
        final TreeItem<TaskViewModel> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final TreeItem<TaskViewModel> parentItem = selectedItem.getParent();
        final int itemPosition = parentItem.getChildren().indexOf(selectedItem);
        if (itemPosition < 0) {
            throw new IllegalArgumentException("can't find selectedItem in parentItem");
        }

        int insertionPos = itemPosition;

        final Iterator<TreeItem<TaskViewModel>> iter = selectedItem.getChildren().iterator();
        while (iter.hasNext()) {
            final TreeItem<TaskViewModel> child = iter.next();
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

}
