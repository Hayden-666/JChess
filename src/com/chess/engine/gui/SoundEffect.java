package com.chess.engine.gui;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
public class SoundEffect {
    public SoundEffect() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File ("music/moveSound.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

}
