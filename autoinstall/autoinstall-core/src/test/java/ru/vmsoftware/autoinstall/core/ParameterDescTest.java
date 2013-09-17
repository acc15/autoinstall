package ru.vmsoftware.autoinstall.core;

import org.junit.Test;
import org.mockito.Mockito;
import ru.vmsoftware.autoinstall.core.params.ParameterDesc;
import ru.vmsoftware.autoinstall.core.task.TaskException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class ParameterDescTest {

    private static final String EXPECTED_NAME = "abc";
    private static final int EXPECTED_DEFAULT_VALUE = 123;
    private static final Class<Integer> EXPECTED_CLASS = Integer.class;
    private static final int CONTEXT_VALUE = 321;

    @Test
    public void testParameterDescWithDefaultValueWorksCorrectly() throws Exception {
        final ParameterDesc<Integer> desc = new ParameterDesc<>(EXPECTED_NAME, EXPECTED_CLASS, EXPECTED_DEFAULT_VALUE);
        assertThat(desc.getType()).isSameAs(EXPECTED_CLASS);
        assertThat(desc.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(desc.getDefaultValue()).isEqualTo(EXPECTED_DEFAULT_VALUE);
        assertThat(desc.isMandatory()).isFalse();
    }

    @Test
    public void testMandatoryParameter() throws Exception {
        final ParameterDesc<Integer> desc = new ParameterDesc<>(EXPECTED_NAME, EXPECTED_CLASS);
        assertThat(desc.getType()).isSameAs(EXPECTED_CLASS);
        assertThat(desc.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(desc.getDefaultValue()).isNull();
        assertThat(desc.isMandatory()).isTrue();
    }

    @Test
    public void testGetValueReturnsParameterFromContext() throws Exception {
        final ExecutionContext context = Mockito.mock(ExecutionContext.class);
        final ParameterDesc<Integer> desc = new ParameterDesc<>(EXPECTED_NAME, EXPECTED_CLASS);
        when(context.getParameter(EXPECTED_NAME, EXPECTED_CLASS)).thenReturn(CONTEXT_VALUE);
        final Integer val = desc.getValue(context);
        assertThat(val).isEqualTo(CONTEXT_VALUE);
        verify(context).getParameter(EXPECTED_NAME, EXPECTED_CLASS);
    }

    @Test
    public void testGetValueReturnsDefaultValueIfValueMissingInContext() throws Exception {
        final ExecutionContext context = Mockito.mock(ExecutionContext.class);
        final ParameterDesc<Integer> desc = new ParameterDesc<>(EXPECTED_NAME, EXPECTED_CLASS, EXPECTED_DEFAULT_VALUE);
        when(context.getParameter(EXPECTED_NAME, EXPECTED_CLASS)).thenReturn(null);
        final Integer val = desc.getValue(context);
        assertThat(val).isEqualTo(EXPECTED_DEFAULT_VALUE);
        verify(context).getParameter(EXPECTED_NAME, EXPECTED_CLASS);
    }

    @Test(expected = TaskException.class)
    public void testGetValueThrowsExceptionForMandatoryParameterWhenValueIsMissingInContext() throws Exception {
        final ExecutionContext context = Mockito.mock(ExecutionContext.class);
        final ParameterDesc<Integer> desc = new ParameterDesc<>(EXPECTED_NAME, EXPECTED_CLASS);
        when(context.getParameter(EXPECTED_NAME, EXPECTED_CLASS)).thenReturn(null);
        desc.getValue(context);
    }
}
