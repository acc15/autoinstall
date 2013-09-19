package ru.vmsoftware.autoinstall.core.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public interface Serializer<T> {

    void serialize(T object, OutputStream outputStream) throws IOException;
    T deserialize(InputStream inputStream) throws IOException;

}
