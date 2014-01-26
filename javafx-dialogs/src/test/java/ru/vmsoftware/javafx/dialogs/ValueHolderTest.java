package ru.vmsoftware.javafx.dialogs;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Vyacheslav Mayorov
 * @since 2013-02-10
 */
public class ValueHolderTest {

    private ValueHolder<String> holder = new ValueHolder<>();

    @Test
    public void testGetSetValue() throws Exception {
        assertThat(holder.getValue()).isNull();
        holder.setValue("test");
        assertThat(holder.getValue()).isEqualTo("test");
    }
}
