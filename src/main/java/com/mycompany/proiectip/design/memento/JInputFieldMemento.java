/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.memento;

import javax.swing.JTextField;

/**
 *
 * @author antonie
 */
 public class JInputFieldMemento {
    private String input;

    public JInputFieldMemento(JTextField inputField) {
        this.input = inputField.getText();
    }

    public String getInput() {
        return input;
    }
}