package ru.vmsoftware.autoinstall.core.task;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import ru.vmsoftware.autoinstall.core.TestData;
import ru.vmsoftware.autoinstall.core.TestUtils;
import ru.vmsoftware.autoinstall.core.serialization.JAXBSerializer;

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

    @Test
    public void testSerialize() throws Exception {
        final String resultingXml;
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            new JAXBSerializer().serialize(TestData.createSampleTask(), os);
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
        final Task expectedTask = TestData.createSampleTask();
        try (final InputStream is = JAXBSerializerTest.class.getResourceAsStream("taskSample1.xml")) {
            final Task actualTask = new JAXBSerializer().deserialize(is);
            TestUtils.assertTask(expectedTask, actualTask);
        }
    }
}
