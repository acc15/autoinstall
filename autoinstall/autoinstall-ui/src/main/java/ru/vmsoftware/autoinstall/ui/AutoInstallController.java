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
import ru.vmsoftware.autoinstall.core.actions.ActionDefinition;
import ru.vmsoftware.autoinstall.core.actions.ActionRegistry;
import ru.vmsoftware.autoinstall.core.actions.DefaultActionRegistry;
import ru.vmsoftware.autoinstall.core.actions.NullAction;
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
    private ComboBox<ActionDefinition<?>> taskActionComboBox;

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

    private Map<ActionDefinition<?>,Image> cachedIcons = new WeakHashMap<>();

    private static ActionRegistry getActionRegistry() {
        return DefaultActionRegistry.getInstance();
    }

    private ImageView getIconForTask(TaskViewModel task) {
        final ActionDefinition<?> definition = task.getActionDefinition();
        Image icon = cachedIcons.get(definition);
        if (icon == null) {
            icon = new Image(
                    AutoInstallController.class.getResource(definition.getName() + "-action.png").toExternalForm());
            cachedIcons.put(definition, icon);
        }
        return new ImageView(icon);
    }

    private CheckBoxTreeItem<TaskViewModel> createTaskItem(String initialDescriptionKey) {
        final CheckBoxTreeItem<TaskViewModel> item = new CheckBoxTreeItem<>();
        final TaskViewModel task = new TaskViewModel();
        task.setDescription(resourceBundle.getString(initialDescriptionKey));
        item.setValue(task);
        item.setGraphic(getIconForTask(task));
        item.setIndependent(true);
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

        final boolean disableAdd = selectedItem.getValue().getActionDefinition() != NullAction.DEFINITION;
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
        taskActionComboBox.setValue(task.getActionDefinition());

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

        taskActionComboBox.setItems(new ObservableListWrapper<>(getActionRegistry().getAvailableActions()));
        taskActionComboBox.setConverter(new StringConverter<ActionDefinition<?>>() {
            @Override
            public String toString(ActionDefinition<?> definition) {
                return resourceBundle.getString(definition != null
                        ? "task." + definition.getName()
                        : "key.chooseTaskType");
            }

            @Override
            public ActionDefinition<?> fromString(String s) {
                return null;
            }
        });
        taskActionComboBox.valueProperty().addListener(new ChangeListener<ActionDefinition<?>>() {
            @Override
            public void changed(ObservableValue<? extends ActionDefinition<?>> observableValue,
                                ActionDefinition<?> oldDefinition,
                                ActionDefinition<?> newDefinition) {
                final TreeItem<TaskViewModel> item = taskList.getSelectionModel().getSelectedItem();
                if (item == null && newDefinition != null) {
                    throw new IllegalStateException("no item is selected in TreeView but task type was modified");
                } else if (item != null && newDefinition == null) {
                    throw new IllegalStateException("item is selected but null definition was selected");
                }

                if (newDefinition == null) {
                    return;
                }

                item.getValue().setActionDefinition(newDefinition);
                item.setGraphic(getIconForTask(item.getValue()));

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
        final int selectedIndex = taskList.getSelectionModel().getSelectedIndex();

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
        taskList.getSelectionModel().select(selectedIndex - (itemPosition + 1));
    }

    @FXML
    public void installClick() {
        System.out.println("install");
    }

}
