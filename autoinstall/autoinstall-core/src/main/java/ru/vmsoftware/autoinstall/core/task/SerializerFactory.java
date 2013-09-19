package ru.vmsoftware.autoinstall.core.task;

import java.io.File;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public interface SerializerFactory<T> {

    Serializer<T> createSerializer(File file);
    List<String> getSupportedExtensions();

}
