package ru.vmsoftware.autoinstall.core.task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class TaskSerializerFactory implements SerializerFactory<Task> {
    @Override
    public Serializer<Task> createSerializer(File file) {
        return new JAXBSerializer();
    }

    @Override
    public List<String> getSupportedExtensions() {
        return Arrays.asList("xml");
    }

}
