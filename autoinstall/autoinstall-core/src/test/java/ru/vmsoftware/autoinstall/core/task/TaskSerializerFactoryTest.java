package ru.vmsoftware.autoinstall.core.task;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.serialization.JAXBSerializer;
import ru.vmsoftware.autoinstall.core.serialization.Serializer;
import ru.vmsoftware.autoinstall.core.serialization.TaskSerializerFactory;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-20-09
 */
public class TaskSerializerFactoryTest {

    @Test
    public void testCreateSerializerReturnsCorrectSerializer() throws Exception {
        final TaskSerializerFactory factory = new TaskSerializerFactory();
        final Serializer<Task> serializer = factory.createSerializer(new File("test"));
        assertThat(serializer).isInstanceOf(JAXBSerializer.class);
    }

    @Test
    public void testGetSupportedExtensionsReturnsListOfSupportedExtensions() throws Exception {
        final TaskSerializerFactory factory = new TaskSerializerFactory();
        assertThat(factory.getSupportedExtensions()).containsExactly("xml");
    }

    @Test
    public void testGetDefaultExtensionReturnsXml() throws Exception {
        final TaskSerializerFactory factory = new TaskSerializerFactory();
        assertThat(factory.getDefaultExtension()).isEqualTo("xml");
    }



}
