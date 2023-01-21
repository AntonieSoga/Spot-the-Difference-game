/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.command;

import java.util.Stack;

/**
 *
 * @author antonie
 */
public class Invoker {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public Invoker() {
        undoStack = new Stack<Command>();
        redoStack = new Stack<Command>();
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }
    
}
