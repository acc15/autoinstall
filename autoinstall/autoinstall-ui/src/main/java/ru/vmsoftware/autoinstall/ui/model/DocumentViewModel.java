package ru.vmsoftware.autoinstall.ui.model;

import ru.vmsoftware.autoinstall.ui.UIEvent;
import ru.vmsoftware.events.Events;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class DocumentViewModel {

    private final String newDocumentPath;
    private String documentPath;
    private boolean modified;

    private void setValues(String path, boolean modified) {
        this.documentPath = path;
        this.modified = modified;
        Events.emit(this, UIEvent.CHANGE);
    }

    public DocumentViewModel(String newDocumentPath) {
        this.newDocumentPath = newDocumentPath;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public boolean isModified() {
        return modified;
    }

    public void markSaved(String path) {
        setValues(path, false);
    }

    public void markNew() {
        setValues(newDocumentPath, true);
    }

    public void markDirty() {
        setValues(documentPath, true);
    }
}
