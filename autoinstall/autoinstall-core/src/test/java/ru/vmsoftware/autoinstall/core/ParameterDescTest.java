package ru.vmsoftware.autoinstall.core;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-17-09
 */
public class ParameterDescTest {

    private static final String EXPECTED_NAME = "abc";
    private static final int EXPECTED_DEFAULT_VALUE = 123;
    private static final Class<Integer> EXPECTED_CLASS = Integer.class;

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
}
