package ru.vmsoftware.autoinstall.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class CheckedBoxTreeCellFactory<T> implements Callback<TreeView<T>, TreeCell<T>> {

    private final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty =
        new Callback<TreeItem<T>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TreeItem<T> taskTreeItem) {
                return ((CheckBoxTreeItem<T>) taskTreeItem).selectedProperty();
            }
        };

    private StringConverter<TreeItem<T>> converter;

    public CheckedBoxTreeCellFactory(StringConverter<TreeItem<T>> converter) {
        this.converter = converter;
    }

    @Override
    public TreeCell<T> call(TreeView<T> taskTreeView) {
        return new CheckBoxTreeCell<T>(getSelectedProperty, converter) {
            @Override
            public void updateItem(T task, boolean empty) {
                super.updateItem(task, empty);
                final TreeItem<T> item = getTreeItem();
                if (item != null && item.getGraphic() != null) {
                    final HBox hBox = HBoxBuilder.create().children(
                            getGraphic(),
                            item.getGraphic()).build();
                    setGraphic(hBox);
                }
            }
        };
    }

}
