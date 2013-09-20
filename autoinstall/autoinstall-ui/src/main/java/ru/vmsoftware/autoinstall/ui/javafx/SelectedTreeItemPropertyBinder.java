package ru.vmsoftware.autoinstall.ui.javafx;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.lang.reflect.InvocationTargetException;

/**
* @author Vyacheslav Mayorov
* @since 2013-20-09
*/
public class SelectedTreeItemPropertyBinder<M,T> implements ChangeListener<T> {
    public SelectedTreeItemPropertyBinder(TreeView<M> model, String propertyName) {
        this.treeView = model;
        this.propertyName = propertyName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void changed(ObservableValue<? extends T> observableValue, T t, T t2) {
        final TreeItem<M> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        final M model = selectedItem.getValue();
        final Property<T> property;
        try {
            property = (Property)model.getClass().getMethod(propertyName + "Property").invoke(model);
        } catch (IllegalAccessException|InvocationTargetException |NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        property.setValue(t2);
    }

    private TreeView<M> treeView;
    private String propertyName;
}
