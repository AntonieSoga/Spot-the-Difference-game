/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.adaper;

/**
 *
 * @author antonie
 */


class KeyboardAdapter implements Button {
    private Button button;
    private int keyCode;

    public KeyboardAdapter(Button button, int keyCode) {
        this.button = button;
        this.keyCode = keyCode;
    }

    public void listenForKeyPress() {
        // Add a key press listener to the keyboard, and call button.click() when the specified key is pressed
    }

    public void click() {
        button.click();
    }
}
