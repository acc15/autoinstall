package ru.vmsoftware.autoinstall.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import ru.vmsoftware.autoinstall.core.action.ActionDefinition;
import ru.vmsoftware.autoinstall.core.action.ActionRegistry;
import ru.vmsoftware.autoinstall.core.action.DefaultActionRegistry;
import ru.vmsoftware.autoinstall.core.action.NullAction;
import ru.vmsoftware.autoinstall.core.task.Task;

import java.net.URL;
import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class AutoInstallController implements Initializable {

    @FXML
    private TreeView<Task> taskList;

    @FXML
    private ComboBox<ActionDefinition<?>> taskActionComboBox;

    @FXML
    private TextField taskDescriptionTextField;

    @FXML
    private MenuItem addTaskMenuItem;

    @FXML
    private MenuItem deleteTaskMenuItem;

    private ResourceBundle resourceBundle;

    private Map<String,Image> cachedIcons = new WeakHashMap<>();

    private static ActionRegistry getActionRegistry() {
        return DefaultActionRegistry.getInstance();
    }

    private ImageView getIconForTask(Task task) {
        final String name = task.getAction().getDefinition().getName();
        Image icon = cachedIcons.get(name);
        if (icon == null) {
            icon = new Image(
                    AutoInstallController.class.getResource(name + "-action.png").toExternalForm());
            cachedIcons.put(name, icon);
        }
        return new ImageView(icon);
    }

    private CheckBoxTreeItem<Task> createTaskItem(String initialDescriptionKey) {

        final CheckBoxTreeItem<Task> item = new CheckBoxTreeItem<>();
        final Task task = new Task(new AbstractList<Task>() {
            @Override
            public Task get(int index) {
                return item.getChildren().get(index).getValue();
            }

            @Override
            public int size() {
                return item.getChildren().size();
            }
        });
        task.setDescription(resourceBundle.getString(initialDescriptionKey));
        item.setValue(task);

        item.setGraphic(getIconForTask(task));
        item.setIndependent(true);
        item.setSelected(task.isActive());
        item.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue,
                                Boolean newValue) {
                task.setActive(newValue);
            }
        });
        return item;
    }

    private void updateMenuItemsForSelectedTreeItem(TreeItem<Task> selectedItem) {
        if (selectedItem == null) {
            deleteTaskMenuItem.setDisable(true);
            addTaskMenuItem.setDisable(true);
            return;
        }
        addTaskMenuItem.setDisable(selectedItem.getValue().getAction() != NullAction.getInstance());
        deleteTaskMenuItem.setDisable(selectedItem.getParent() == null);
    }

    private static void fireChangeEvent(TreeItem<Task> selectedItem) {
        Event.fireEvent(selectedItem, new TreeItem.TreeModificationEvent<>(
                TreeItem.valueChangedEvent(), selectedItem, selectedItem.getValue()));
    }

    @FXML
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {

        this.resourceBundle = resourceBundle;

        taskList.setCellFactory(new CheckedBoxTreeCellFactory<>(new StringConverter<TreeItem<Task>>() {
            @Override
            public String toString(TreeItem<Task> taskTreeItem) {
                return taskTreeItem.getValue().getDescription();
            }

            @Override
            public TreeItem<Task> fromString(String s) {
                return null;
            }
        }));
        taskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Task>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Task>> observableValue,
                                TreeItem<Task> oldItem,
                                TreeItem<Task> newItem) {
                updateMenuItemsForSelectedTreeItem(newItem);
                if (newItem == null) {
                    taskDescriptionTextField.setDisable(true);
                    taskDescriptionTextField.setText(null);
                    taskActionComboBox.setDisable(true);
                    taskActionComboBox.setValue(null);
                    return;
                }

                taskDescriptionTextField.setDisable(false);

                final Task task = newItem.getValue();
                taskDescriptionTextField.setText(task.getDescription());
                taskActionComboBox.setDisable(!newItem.getChildren().isEmpty());
                taskActionComboBox.setValue(task.getAction().getDefinition());
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
                final TreeItem<Task> item = taskList.getSelectionModel().getSelectedItem();
                if (item == null && newDefinition != null) {
                    throw new IllegalStateException("no item is selected in TreeView but task type was modified");
                } else if (item != null && newDefinition == null) {
                    throw new IllegalStateException("item is selected but null definition was selected");
                }

                if (newDefinition == null) {
                    return;
                }

                item.getValue().setAction(newDefinition.getAction());
                item.setGraphic(getIconForTask(item.getValue()));
                updateMenuItemsForSelectedTreeItem(item);
                fireChangeEvent(item);

            }
        });

        taskDescriptionTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                final TreeItem<Task> selectedItem = taskList.getSelectionModel().getSelectedItem();
                if (selectedItem == null) {
                    return;
                }

                final Task task = selectedItem.getValue();
                task.setDescription(newValue);
                fireChangeEvent(selectedItem);
            }
        });

    }

    @FXML
    public void addTaskClick() {
        final TreeItem<Task> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final CheckBoxTreeItem<Task> newItem = createTaskItem("task.initialDescription");
        selectedItem.getChildren().add(newItem);
        taskList.getSelectionModel().select(newItem);
    }

    @FXML
    public void deleteTaskClick() {
        final TreeItem<Task> selectedItem = taskList.getSelectionModel().getSelectedItem();
        final TreeItem<Task> parentItem = selectedItem.getParent();
        final int itemPosition = parentItem.getChildren().indexOf(selectedItem);
        if (itemPosition < 0) {
            throw new IllegalArgumentException("can't find selectedItem in parentItem");
        }

        int insertionPos = itemPosition;

        final Iterator<TreeItem<Task>> iter = selectedItem.getChildren().iterator();
        while (iter.hasNext()) {
            final TreeItem<Task> child = iter.next();
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
