package ru.vmsoftware.autoinstall.core.task;

import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;
import ru.vmsoftware.autoinstall.task.JAXBTask;
import ru.vmsoftware.autoinstall.task.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class JAXBSerializer implements Serializer<Task> {

    private JAXBContext jaxbContext;

    private ObjectFactory factory = new ObjectFactory();

    public JAXBSerializer() {
        this.jaxbContext = createJAXBContext();
    }

    private static String emptyAsNull(String str) {
        return str == null || str.isEmpty() ? null : str;
    }

    private static String nullAsEmpty(String str) {
        return str == null ? "" : str;
    }

    private JAXBTask mapDomainToJAXB(Task task) {
        final JAXBTask jaxbTask = factory.createJAXBTask();
        jaxbTask.setDescription(emptyAsNull(task.getDescription()));
        jaxbTask.setConditions(emptyAsNull(task.getConditions()));
        jaxbTask.setActive(task.isActive() ? null : false);
        jaxbTask.setType(task.getActionType() != ActionType.NULL ? task.getActionType().getName() : null);
        for (Parameter parameter: task.getParameters()) {
            final JAXBTask.Parameter jaxbParameter = factory.createJAXBTaskParameter();
            jaxbParameter.setName(parameter.getName());
            jaxbParameter.setValue(parameter.getValue());
            jaxbTask.getParameter().add(jaxbParameter);
        }
        for (Task child: task.getChildren()) {
            final JAXBTask jaxbTaskChild = mapDomainToJAXB(child);
            jaxbTask.getTask().add(jaxbTaskChild);
        }
        return jaxbTask;
    }

    private Task mapJAXBToDomain(JAXBTask jaxbTask) {
        final Task task = new Task();
        task.setDescription(nullAsEmpty(jaxbTask.getDescription()));
        task.setConditions(nullAsEmpty(jaxbTask.getConditions()));
        task.setActive(jaxbTask.isActive());
        task.setActionType(ActionType.getDefinitionByName(jaxbTask.getType()));
        for (final JAXBTask.Parameter jaxbParameter: jaxbTask.getParameter()) {
            task.getParameters().add(
                    new Parameter(jaxbParameter.getName(), jaxbParameter.getValue()));
        }
        for (final JAXBTask jaxbChild: jaxbTask.getTask()) {
            task.getChildren().add(mapJAXBToDomain(jaxbChild));
        }
        return task;
    }

    private static JAXBContext createJAXBContext() {
        final JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(JAXBTask.class);
        } catch (JAXBException e) {
            throw new RuntimeException("unable to create new JAXBContext", e);
        }
        return jaxbContext;
    }

    @Override
    public void serialize(Task object, OutputStream outputStream) throws IOException {
        final Marshaller marshaller;
        try {
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", true);
        } catch (JAXBException e) {
            throw new RuntimeException("unable to create and initialize JAXB marshaller", e);
        }

        final JAXBTask jaxbTask = mapDomainToJAXB(object);
        try {
            marshaller.marshal(jaxbTask, outputStream);
        } catch (JAXBException e) {
            throw new IOException("unable to marshal task", e);
        }
    }

    @Override
    public Task deserialize(InputStream inputStream) throws IOException {
        final Unmarshaller unmarshaller;
        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("unable to create JAXB unmarshaller", e);
        }

        final JAXBTask jaxbTask;
        try {
            jaxbTask = (JAXBTask)unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new IOException("unable to unmarshal task", e);
        }

        return mapJAXBToDomain(jaxbTask);
    }
}
