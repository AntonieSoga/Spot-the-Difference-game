/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proiectip;

/**
 *
 * @author antonie
 */
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MusicTest {

    @Test
    public void testPlay() throws IOException {
        Music music = new Music();
        music.setFile("graphics/sounds/TremLoadingloopl.wav");
        music.play(false);
        assertTrue(music.clip.isRunning());
        music.stop();
        assertFalse(music.clip.isRunning());
    }

    @Test
    public void testLoop() throws IOException {
        Music music = new Music();
        music.setFile("graphics/sounds/Timetick.ogg");
        music.play(true);
        assertTrue(music.clip.isRunning());
        assertTrue(music.clip.isActive());
        music.stop();
        assertFalse(music.clip.isRunning());
    }
}

