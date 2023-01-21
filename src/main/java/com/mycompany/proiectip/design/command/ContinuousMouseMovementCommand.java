/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip.design.command;

    import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContinuousMouseMovementCommand implements Command {
    private Robot robot;
    private boolean isRunning = true;
    private int centerX, centerY, radius;

    public ContinuousMouseMovementCommand(int centerX, int centerY, int radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 
    @Override
    public void execute() {
        double angle = 0;
        int c=0;
        while (isRunning) {
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            robot.mouseMove(x, y);
            angle += 0.1;
            c++;
            if(c==70)
                isRunning=false;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}