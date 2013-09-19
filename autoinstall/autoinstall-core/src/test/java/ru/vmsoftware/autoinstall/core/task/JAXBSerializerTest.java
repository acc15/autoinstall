package ru.vmsoftware.autoinstall.core.task;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import ru.vmsoftware.autoinstall.core.actions.ActionType;
import ru.vmsoftware.autoinstall.core.params.Parameter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-19-09
 */
public class JAXBSerializerTest {

    static {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
    }

    private void assertTask(Task expectedTask, Task actualTask, String prefix) {

        assertThat(actualTask.getDescription()).as(prefix + ".description").
                isEqualTo(expectedTask.getDescription());
        assertThat(actualTask.getConditions()).as(prefix + ".conditions").
                isEqualTo(expectedTask.getConditions());
        assertThat(actualTask.getActionType()).as(prefix + ".actionType").
                isEqualTo(expectedTask.getActionType());
        assertThat(actualTask.isActive()).as(prefix + ".active").
                isEqualTo(expectedTask.isActive());

        assertThat(actualTask.getParameters()).as(prefix + "parameters.size").
                hasSize(expectedTask.getParameters().size());

        for (int i=0; i<expectedTask.getParameters().size(); i++) {
            final Parameter expectedParameter = expectedTask.getParameters().get(i);
            final Parameter actualParameter = actualTask.getParameters().get(i);
            assertThat(expectedParameter.getName()).as(prefix + ".parameter[" + i + "].name").
                    isEqualTo(actualParameter.getName());
            assertThat(expectedParameter.getValue()).as(prefix + ".parameter[" + i + "].value").
                    isEqualTo(actualParameter.getValue());
        }

        assertThat(actualTask.getChildren()).hasSize(expectedTask.getChildren().size());
        for (int i=0; i<expectedTask.getChildren().size(); i++) {
            final Task expectedChild = expectedTask.getChildren().get(i);
            final Task actualChild = actualTask.getChildren().get(i);
            assertTask(expectedChild, actualChild, prefix + ".children[" + i + "]");
        }

    }

    private void assertTask(Task expectedTask, Task actualTask) {
        assertTask(expectedTask, actualTask, "task");
    }

    private Task createSampleTask() {
        final Task expectedTask = new Task();
        expectedTask.setDescription("String");
        expectedTask.setConditions("String");

        final Parameter p1 = new Parameter("abc", "hjuhukio");
        expectedTask.getParameters().add(p1);

        final Task c1 = new Task();
        c1.setActive(false);
        c1.setDescription("test task");
        c1.setConditions("a == b");
        expectedTask.getChildren().add(c1);

        final Task c11 = new Task();
        c11.setActionType(ActionType.EXECUTE);

        final Parameter p11 = new Parameter("abc", "hjuhukio");
        c11.getParameters().add(p11);
        c1.getChildren().add(c11);
        return expectedTask;
    }

    @Test
    public void testSerialize() throws Exception {
        final String resultingXml;
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            new JAXBSerializer().serialize(createSampleTask(), os);
            resultingXml = os.toString("utf-8");
        }

        System.out.println("Serialized document: " + resultingXml);

        final Document testDocument = XMLUnit.buildTestDocument(resultingXml);
        final Document controlDocument = XMLUnit.buildControlDocument(new InputSource(
                JAXBSerializerTest.class.getResource("taskSample1.xml").toExternalForm()));

        final Diff diff = XMLUnit.compareXML(controlDocument, testDocument);
        diff.overrideDifferenceListener(new DifferenceListener() {
            @Override
            public int differenceFound(Difference difference) {
                final String expectedPath = difference.getControlNodeDetail().getXpathLocation();
                if ("/task[1]/@schemaLocation".equals(expectedPath)) {
                    return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
                }
                return RETURN_ACCEPT_DIFFERENCE;
            }

            @Override
            public void skippedComparison(Node node, Node node2) {
            }
        });
        assertThat(diff.identical()).as(diff.toString()).isTrue();
    }

    @Test
    public void testDeserialize() throws Exception {
        final Task expectedTask = createSampleTask();
        try (final InputStream is = JAXBSerializerTest.class.getResourceAsStream("taskSample1.xml")) {
            final Task actualTask = new JAXBSerializer().deserialize(is);
            assertTask(expectedTask, actualTask);
        }
    }
}
