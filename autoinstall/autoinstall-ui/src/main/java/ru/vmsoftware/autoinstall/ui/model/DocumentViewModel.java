package ru.vmsoftware.autoinstall.ui.model;

import ru.vmsoftware.autoinstall.ui.UIEvent;
import ru.vmsoftware.events.Events;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class DocumentViewModel {

    public static final String KEY_NEW_NAME = "key.newName";

    private String documentPath;
    private boolean isNew;
    private boolean modified;

    private void setValues(String path, boolean modified) {
        if (path.equals(documentPath) && this.modified == modified) {
            return;
        }
        this.documentPath = path;
        this.modified = modified;
        Events.emit(this, UIEvent.CHANGE);
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isNew() {
        return isNew;
    }

    public void markSaved(String path) {
        setValues(path, false);
        this.isNew = false;
    }

    public void markNew(String path) {
        setValues(path, true);
        this.isNew = true;
    }

    public void markDirty() {
        setValues(documentPath, true);
    }
}
