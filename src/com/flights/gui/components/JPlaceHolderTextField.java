package com.flights.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * JPlaceHolderTextField is alike a regular JTextField however it has a placeholder associated with it
 * <br>Placeholder is stored as a string in each instance of JPlaceHolderTextField
 */
public class JPlaceHolderTextField extends JTextField implements FocusListener {

    // placeholder
    private String placeholder;

    /**
     * Construct a new JPlaceHolderTextField
     * @param placeholder String
     */
    public JPlaceHolderTextField(String placeholder) {
        super(placeholder);
        setForeground(Color.GRAY);
        this.placeholder = placeholder;
        this.addFocusListener(this);
    }

    /**
     * Construct a new JPlaceHolderTextField
     * @param placeholder String
     * @param columns width in columns
     */
    public JPlaceHolderTextField(String placeholder, int columns) {
        super(placeholder, columns);
        setForeground(Color.GRAY);
        this.placeholder = placeholder;
        this.addFocusListener(this);
    }

    /**
     * Gets placeholder text
     * @return String
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Set placeholder text
     * @param placeholder String
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Get text field text
     * @return String
     */
    @Override
    public String getText() {
        // return get text only if the placeholder isn't the same as the text
        String s = super.getText();
        return s.equals(placeholder) ? "" : s;
    }

    /**
     * Check if the focus gained and the placeholder is still in place
     * <br>If placeholder is in place then clear the text field
     * @param e the event to be processed
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (super.getText().equals(placeholder)) {
            setForeground(Color.BLACK);
            setText("");
        }
    }

    /**
     * Check if focus is lost and if nothing is typed, replace with placeholder
     * <br>If there is text entered, textfield is not cleared
     * @param e the event to be processed
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (super.getText().isEmpty()) {
            setForeground(Color.GRAY);
            setText(placeholder);
        }
    }

}
