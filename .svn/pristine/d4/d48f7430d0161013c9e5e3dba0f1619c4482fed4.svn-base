/*
 * TCSS 305 Assignment 6 - Tetris
 */

package sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Sound for Tetris.
 *
 * @author Tzu-Chien Lu
 * @version Dec 11 2015
 */
public enum Sound {
    
    /** Music when game is played. */
    GAME("CakeByTheOcean.wav"),
    
    /** A sound is used for movement and rotation. */
    MOVE("move.wav"),
    
    /** A sound is used for hard drop. */
    DROP("drop.wav");
    
    /** Each sound effect has its own clip, loaded with its own sound file. */
    private Clip myClip;

    /**
     * Constructor to construct each element of the enum with its own sound file.
     * 
     * @param theSoundFile the location of sound file.
     */
    Sound(final String theSoundFile) {
       
        try {
            
            /** Use URL to read music from disk and JAR. */
            final URL url = this.getClass().getResource(theSoundFile);
            
            /** Set up an audio input. */
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            
            /** Get the clip from resource.*/
            myClip = AudioSystem.getClip();
            
            /** Open audio clip and load samples. */
            myClip.open(audioInputStream);
            
        } catch (final UnsupportedAudioFileException e) {
            e.printStackTrace();
            
        } catch (final IOException e) {
            e.printStackTrace();
            
        } catch (final LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Play or Re-play the sound effect from the beginning, by rewinding.
     */
    public void play() {
        
        if (GAME.name().equals(name())) {
            myClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
            
        /** Rewind to the beginning.*/
        myClip.setFramePosition(0); 
        myClip.start();     
    }
    
    /**
     * Optional static method to pre-load all the sound files.
     */
    public static void init() {
        
        /** Calls the constructor for all the elements. */
        values(); 
    }
    
    /**
     * Stop the music.
     */
    public void stop() {
        myClip.stop();
        
        /** Rewind to the beginning.*/
        myClip.setFramePosition(0);
    }
}
