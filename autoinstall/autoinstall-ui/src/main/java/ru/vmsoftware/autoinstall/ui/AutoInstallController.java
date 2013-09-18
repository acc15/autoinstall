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
import ru.vmsoftware.autoinstall.core.task.Task;
import ru.vmsoftware.autoinstall.core.task.TaskDefinition;
import ru.vmsoftware.autoinstall.core.task.TaskUtils;
import ru.vmsoftware.autoinstall.core.task.registry.DefaultTaskRegistry;
import ru.vmsoftware.autoinstall.core.task.registry.TaskRegistry;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class AutoInstallController implements Initializable {

    @FXML
    private TreeView<Task> taskList;

    @FXML
    private ComboBox<TaskDefinition<?>> taskTypeComboBox;

    @FXML
    private TextField taskDescriptionTextField;

    @FXML
    private MenuItem addTaskMenuItem;

    @FXML
    private MenuItem deleteTaskMenuItem;

    private ResourceBundle resourceBundle;


    private Map<String,Image> cachedIcons = new WeakHashMap<>();

    private static TaskRegistry getTaskFactory() {
        return DefaultTaskRegistry.getInstance();
    }

    private ImageView getIconForTask(TaskDefinition<?> taskDefinition) {
        final String name = taskDefinition.getName();
        Image icon = cachedIcons.get(name);
        if (icon == null) {
            icon = new Image(
                    AutoInstallController.class.getResource(name + ".png").toExternalForm());
            cachedIcons.put(name, icon);
        }
        return new ImageView(icon);
    }

    private CheckBoxTreeItem<Task> createTaskItem(String initialDescriptionKey) {
        final Task newTask = getTaskFactory().getAvailableTasks().get(0).createTask();
        newTask.setDescription(resourceBundle.getString(initialDescriptionKey));

        final CheckBoxTreeItem<Task> item = new CheckBoxTreeItem<>(newTask, getIconForTask(newTask.getDefinition()));
        item.setIndependent(true);
        item.setSelected(newTask.isActive());
        item.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                                Boolean oldValue,
                                Boolean newValue) {
                newTask.setActive(newValue);
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
        addTaskMenuItem.setDisable(selectedItem.getValue().getChildren() == null);
        deleteTaskMenuItem.setDisable(selectedItem.getParent() == null);
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
                    taskTypeComboBox.setDisable(true);
                    taskTypeComboBox.setValue(null);
                    return;
                }

                taskDescriptionTextField.setDisable(false);

                final Task task = newItem.getValue();
                taskDescriptionTextField.setText(task.getDescription());
                taskTypeComboBox.setDisable(!newItem.getChildren().isEmpty());
                taskTypeComboBox.setValue(task.getDefinition());
            }
        });

        taskList.setRoot(createTaskItem("task.rootInitialDescription"));

        taskTypeComboBox.setItems(new ObservableListWrapper<>(getTaskFactory().getAvailableTasks()));
        taskTypeComboBox.setConverter(new StringConverter<TaskDefinition<?>>() {
            @Override
            public String toString(TaskDefinition<?> definition) {
                return resourceBundle.getString(definition != null
                        ? "task." + definition.getName()
                        : "key.chooseTaskType");
            }

            @Override
            public TaskDefinition<?> fromString(String s) {
                return null;
            }
        });
        taskTypeComboBox.valueProperty().addListener(new ChangeListener<TaskDefinition<?>>() {
            @Override
            public void changed(ObservableValue<? extends TaskDefinition<?>> observableValue,
                                TaskDefinition<?> oldDefinition,
                                TaskDefinition<?> newDefinition) {
                final TreeItem<Task> item = taskList.getSelectionModel().getSelectedItem();
                if (item == null && newDefinition != null) {
                    throw new IllegalStateException("no item is selected in TreeView but task type was modified");
                } else if (item != null && newDefinition == null) {
                    throw new IllegalStateException("item is selected but null definition was selected");
                }

                if (newDefinition == null) {
                    return;
                }

                final Task newTask = newDefinition.createTask();
                TaskUtils.copyTaskData(item.getValue(), newTask);
                item.setGraphic(getIconForTask(newTask.getDefinition()));
                item.setValue(newTask);
                updateMenuItemsForSelectedTreeItem(item);

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
                Event.fireEvent(selectedItem, new TreeItem.TreeModificationEvent<>(
                        TreeItem.valueChangedEvent(), selectedItem, task));
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


    }

    @FXML
    public void installClick() {
        System.out.println("install");
    }

}
