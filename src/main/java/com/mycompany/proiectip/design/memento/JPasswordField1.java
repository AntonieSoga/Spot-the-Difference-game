/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.memento;

import javax.swing.JPasswordField;

/**
 *
 * @author antonie
 */
public class JPasswordField1 {

    private JPasswordField passwordField;

    public JPasswordField1(String password) {
        passwordField = new JPasswordField(password);
    }

    public JPasswordFieldMemento createMemento() {
        return new JPasswordFieldMemento(passwordField);
    }

    public void restoreMemento(JPasswordFieldMemento memento) {
        passwordField.setText(new String(memento.getPassword()));
    }

    public void setText(String passwordField) {
        this.passwordField.setText(passwordField);
    }
     public String getText()
     {
         return passwordField.getText();
     }
}
