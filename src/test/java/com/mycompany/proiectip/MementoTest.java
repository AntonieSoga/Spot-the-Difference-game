/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip;

/**
 *
 * @author antonie
 */
import com.mycompany.proiectip.design.memento.JInputFieldMemento;
import com.mycompany.proiectip.design.memento.JPasswordFieldMemento;
import com.mycompany.proiectip.design.memento.JInputField1;
import com.mycompany.proiectip.design.memento.JPasswordField1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MementoTest {

    @Test
    public void testJPasswordField() {
        JPasswordField1 passwordField = new JPasswordField1("password123");
        JInputField1 inputField = new JInputField1("input text");

        // Create a memento for the current state of the fields
        JPasswordFieldMemento passwordMemento = passwordField.createMemento();
        JInputFieldMemento inputMemento = inputField.createMemento();

        // Modify the fields
        passwordField.setText("newpassword456");
        inputField.setText("new input text");

        // Restore the fields to their previous state using the mementos
        passwordField.restoreMemento(passwordMemento);
        inputField.restoreMemento(inputMemento);

        // Assert that the fields have been restored correctly
        assertEquals("password123", new String(passwordField.getText()));
        assertEquals("input text", inputField.getText());
    }
}
