/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.adaper;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

/**
 *
 * @author antonie
 */


public class KeyboardAdapter implements QButton {
    private QButton button;
    private int keyCode;

    public KeyboardAdapter(QButton button, int keyCode) {
        this.button = button;
        this.keyCode = keyCode;
    }

    public void listenForKeyPress() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == keyCode) {
                    button.click();
                }
                return false;
            }
        });
    }

    public void click() {
        button.click();
    }
}

