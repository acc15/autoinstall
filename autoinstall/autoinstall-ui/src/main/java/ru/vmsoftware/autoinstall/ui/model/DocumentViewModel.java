package ru.vmsoftware.autoinstall.ui.model;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class DocumentViewModel {

    public static final String KEY_NEW_NAME = "key.newName";

    public static interface OnChangeListener {
        void onChange();
    }

    private String documentPath;
    private boolean isNew;
    private boolean modified;
    private OnChangeListener listener;

    private void setValues(String path, boolean modified) {
        if (path.equals(documentPath) && this.modified == modified) {
            return;
        }
        this.documentPath = path;
        this.modified = modified;
        if (listener != null) {
            listener.onChange();
        }
    }

    public OnChangeListener getListener() {
        return listener;
    }

    public void setListener(OnChangeListener listener) {
        this.listener = listener;
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
        setValues(path, false);
        this.isNew = true;
    }

    public void markDirty() {
        setValues(documentPath, true);
    }
}
