package ru.vmsoftware.javafx.dialogs;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.fest.reflect.core.Reflection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.vmsoftware.javafx.dialogs.bundle.EmptyBundle;
import ru.vmsoftware.javafx.dialogs.bundle.TestBundle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-02-10
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class DialogTest {

    public static final String TEST_TITLE = "testTitle";
    public static final String TEST_MESSAGE = "testMessage";
    public static final String TEST_DETAILS_LABEL = "testDetailsLabel";
    public static final String TEST_DETAILS = "testDetails";
    public static final String TEST_DEFAULT_BUTTON = "testDefaultButton";
    public static final String TEST_CANCEL_BUTTON = "testCancelButton";
    public static final String TEST_BUTTON = "testButton";
    public static final DialogIcon TEST_ICON = DialogIcon.WARNING;

    @Mock
    private Window owner;
    private ValueHolder<DialogResult> valueHolder = new ValueHolder<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalStateException.class)
    public void testButtonThrowsExceptionIfDefaultButtonAlreadyAdded() throws Exception {
        Dialog.withDefault(DialogResult.OK).defaultButton(DialogResult.OK).defaultButton(DialogResult.CLOSED);
    }

    @Test(expected = IllegalStateException.class)
    public void testButtonThrowsExceptionIfCancelButtonAlreadyAdded() throws Exception {
        Dialog.withDefault(DialogResult.OK).cancelButton(DialogResult.OK).cancelButton(DialogResult.CLOSED);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildThrowsIllegalStateExceptionIfResourceBundleMissingAndButtonLabelNotSpecified() throws Exception {
        Dialog.withDefault(DialogResult.CLOSED).
                button(DialogResult.OK).
                build(owner, valueHolder);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildThrowsIllegalStateExceptionIfResourceBundleMissingAndDetailsLabelNotSpecified() throws Exception {
        Dialog.withDefault(DialogResult.CLOSED).
                details(TEST_DETAILS).
                build(owner, valueHolder);
    }

    @Test(expected = MissingResourceException.class)
    public void testBuildThrowsMissingResourceExceptionIfButtonLabelNotSpecified() throws Exception {
        Dialog.withDefault(DialogResult.CLOSED).
                resources(ResourceBundle.getBundle(EmptyBundle.class.getName())).
                button(DialogResult.OK).
                build(owner, valueHolder);
    }

    @Test(expected = MissingResourceException.class)
    public void testBuildThrowsMissingResourceExceptionIfDetailsLabelNotSpecified() throws Exception {
        Dialog.withDefault(DialogResult.CLOSED).
                resources(ResourceBundle.getBundle(EmptyBundle.class.getName())).
                details(TEST_DETAILS).
                build(owner, valueHolder);
    }

    @Test
    public void testBuildWithValuesFromResourceBundle() throws Exception {
        assertDialog(Dialog.withDefault(DialogResult.CLOSED).
                resources(ResourceBundle.getBundle(TestBundle.class.getName()), "custom.").
                details(TEST_DETAILS).
                defaultButton(DialogResult.OK).
                cancelButton(DialogResult.CANCEL).
                button(DialogResult.CLOSED).
                build(owner, valueHolder));
    }

    @Test
    public void testBuildWithSpecifiedParameters() throws Exception {
        assertDialog(Dialog.withDefault(DialogResult.CLOSED).
                title(TEST_TITLE).
                icon(TEST_ICON).
                message(TEST_MESSAGE).
                details(TEST_DETAILS_LABEL, TEST_DETAILS).
                defaultButton(TEST_DEFAULT_BUTTON, DialogResult.OK).
                cancelButton(TEST_CANCEL_BUTTON, DialogResult.CANCEL).
                button(TEST_BUTTON, DialogResult.CLOSED).
                build(owner, valueHolder));
    }


    private void assertDialog(Stage stage) {

        assertThat(stage.getTitle()).isEqualTo(TEST_TITLE);
        final Parent root = stage.getScene().getRoot();
        final ImageView imageView = findNodeInDepth(root, 0, 0);

        final String imageUrl = Reflection.field("url").ofType(String.class).in(imageView.getImage()).get();
        assertThat(imageUrl).isEqualTo(Dialog.class.getResource(TEST_ICON.getPath()).toExternalForm());

        final Label label = findNodeInDepth(root, 0, 1);
        assertThat(label.getText()).isEqualTo(TEST_MESSAGE);

        final TitledPane detailsPane = findNodeInDepth(root, 1);
        assertThat(detailsPane.getText()).isEqualTo(TEST_DETAILS_LABEL);

        final TextArea detailsArea = findNodeInDepth(detailsPane.getContent());
        assertThat(detailsArea.getText()).isEqualTo(TEST_DETAILS);

        final Button defaultButton = findNodeInDepth(root, 2, 0);
        assertThat(defaultButton.isDefaultButton()).isTrue();
        assertThat(defaultButton.getText()).isEqualTo(TEST_DEFAULT_BUTTON);

        final Button cancelButton = findNodeInDepth(root, 2, 1);
        assertThat(cancelButton.isCancelButton()).isTrue();
        assertThat(cancelButton.getText()).isEqualTo(TEST_CANCEL_BUTTON);

        final Button button = findNodeInDepth(root, 2, 2);
        assertThat(button.isDefaultButton()).isFalse();
        assertThat(button.isCancelButton()).isFalse();
        assertThat(button.getText()).isEqualTo(TEST_BUTTON);

        assertThat(valueHolder.getValue()).isEqualTo(DialogResult.CLOSED);

    }

    @SuppressWarnings("unchecked")
    private static <T> T findNodeInDepth(Node node, int... indexes) {
        for (int index: indexes) {
            node = ((Parent)node).getChildrenUnmodifiable().get(index);
        }
        return (T)node;
    }
}
