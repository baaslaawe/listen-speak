package org.snitko.app.playback;

/**
 * Created by vadim on 1/31/16.
 */

import java.io.*;

import sun.audio.*;


/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 */
public class PlaySound {
    public void playWav(String soundFileToPlay) throws IOException {
        InputStream in = new FileInputStream(soundFileToPlay);
        play(in);
    }

    private void play(InputStream in) throws IOException {
        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(in);
        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);
    }


    public static void main(String[] args)
            throws Exception {
        // expect a sound file as args[0]
        if (args.length != 1) {
            System.err.println("I need just one arg, the name of a sound file to play.");
            System.exit(1);
        }
        System.setProperty("java.awt.headless", "true");

        PlaySound playSound = new PlaySound();
        playSound.playWav(args[0]);
    }
}