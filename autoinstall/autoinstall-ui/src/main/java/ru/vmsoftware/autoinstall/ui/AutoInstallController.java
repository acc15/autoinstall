package ru.vmsoftware.autoinstall.ui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class AutoInstallController {

    @FXML
    TreeView<String> taskList;

    @FXML
    public void initialize() {
        final CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>("Root");
        taskList.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        taskList.setRoot(item);
    }

    @FXML
    public void installClick() {
        System.out.println("install");
    }


}
