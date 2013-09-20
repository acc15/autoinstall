package ru.vmsoftware.autoinstall.core.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class SerializationUtils {

    public static <T> void serializeToFile(SerializerFactory<T> factory, File file, T object) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            factory.createSerializer(file).serialize(object, fileOutputStream);
        }
    }

    public static <T> T deserializeFromFile(SerializerFactory<T> factory, File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return factory.createSerializer(file).deserialize(fileInputStream);
        }
    }

}
