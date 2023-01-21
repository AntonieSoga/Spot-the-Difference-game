/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.memento;

/**
 *
 * @author antonie
 */
import javax.swing.JTextField;

public class JInputField1 {

    private JTextField inputField;

    public JInputField1(String input) {
        inputField = new JTextField(input);
    }

    public JInputFieldMemento createMemento() {
        return new JInputFieldMemento(inputField);
    }

    public void restoreMemento(JInputFieldMemento memento) {
        inputField.setText(memento.getInput());
    }
     public void setText(String inputField) {
        this.inputField.setText(inputField);
    }
     
     public String getText()
     {
         return inputField.getText();
     }
}
