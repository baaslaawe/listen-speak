package org.snitko.app.playback;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) [2015] [Vadim Snitkovsky]
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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