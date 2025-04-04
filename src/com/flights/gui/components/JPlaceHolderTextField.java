package com.flights.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// JPlaceHolderTextField is alike a regular JTextField however it has a placeholder associated with it
// Placeholder is stored as a string in each instance of JPlaceHolderTextField
public class JPlaceHolderTextField extends JTextField implements FocusListener {

    // placeholder
    private String placeholder;

    // constructors to match JTextField Constructors
    public JPlaceHolderTextField() {
        super();
        setForeground(Color.GRAY);     // set the color to gray for the placeholder
        setText(placeholder);
        this.addFocusListener(this);     // add the focus listener
    }

    public JPlaceHolderTextField(String placeholder) {
        super(placeholder);
        setForeground(Color.GRAY);
        this.placeholder = placeholder;
        this.addFocusListener(this);
    }

    public JPlaceHolderTextField(String placeholder, int columns) {
        super(placeholder, columns);
        setForeground(Color.GRAY);
        this.placeholder = placeholder;
        this.addFocusListener(this);
    }

    // get placeholder
    public String getPlaceholder() {
        return placeholder;
    }

    // set placeholder
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    // return get text only if the placeholder isn't the same as the text
    @Override
    public String getText() {
        String s = super.getText();
        return s.equals(placeholder) ? "" : s;
    }


        // Check if the focus gained and the placeholder is still in place
        // if placeholder is in place then clear the textfield
    @Override
    public void focusGained(FocusEvent e) {
        if (super.getText().equals(placeholder)) {
            setForeground(Color.BLACK);
            setText("");

        }
    }

    // Check if focus is lost and if nothing is typed, replace with placeholder
    // if there is text entered, textfield is not cleared
    @Override
    public void focusLost(FocusEvent e) {
        if (super.getText().isEmpty()) {
            setForeground(Color.GRAY);
            setText(placeholder);
        }
    }

}
