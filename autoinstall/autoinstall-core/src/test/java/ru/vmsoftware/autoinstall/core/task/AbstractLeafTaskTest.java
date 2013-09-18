package ru.vmsoftware.autoinstall.core.task;

import org.junit.Test;
import ru.vmsoftware.autoinstall.core.ExecutionContext;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-18-09
 */
public class AbstractLeafTaskTest {

    private class TestTask extends AbstractLeafTask {
        @Override
        public void execute(ExecutionContext context) throws TaskException {
        }

        @Override
        public TaskDefinition<?> getDefinition() {
            return null;
        }
    }

    @Test
    public void testGetChildrenReturnsNull() throws Exception {
        final TestTask task = new TestTask();
        assertThat(task.getChildren()).isNull();
    }

}
