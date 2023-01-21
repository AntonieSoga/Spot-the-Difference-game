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
public class JPasswordFieldMemento {
    private char[] password;

    public JPasswordFieldMemento(JPasswordField passwordField) {
        this.password = passwordField.getPassword();
    }

    public char[] getPassword() {
        return password;
    }
}