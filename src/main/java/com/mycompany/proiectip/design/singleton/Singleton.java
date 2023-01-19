
package com.mycompany.proiectip.design.singleton;

import com.mycompany.proiectip.Music;

/**
 *
 * @author antonie
 */
public class Singleton {

    private static final Singleton instance = new Singleton();
    Boolean soundValue;
    int hints;
    public Music bgMusic;
    int gasit;

    // private constructor to avoid client applications using the constructor
    public Singleton() {
        soundValue = true;
        hints = 0;
        bgMusic= new Music();
        gasit=0;
    }

    public void  setGasitValue(int gasit)
    {
        this.gasit=gasit;
    }
    
    public int getGasitValue()
    {
        return this.gasit;
    }
    public Boolean getSoundValue() {
        return soundValue;
    }

    public void setSoundValue(Boolean sv) {
        this.soundValue = sv;
    }

    public int getHints() {
        return hints;
    }

    public void setHints(int hints) {
        this.hints = hints;
    }
    public static Singleton getInstance() {
        return instance;
    }
}
