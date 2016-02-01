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

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 */
public class PlaySound {

    public void play(String soundFileToPlay) throws IOException {
        File in = new File(soundFileToPlay);
        play(in);
    }

    public void play(File inputFile) {

        try (final AudioInputStream in = AudioSystem.getAudioInputStream(inputFile)) {

            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info)) {

                if (sourceDataLine != null) {
                    sourceDataLine.open(outFormat);
                    sourceDataLine.start();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(outFormat, in);
                    stream(audioInputStream, sourceDataLine);
                    sourceDataLine.drain();
                    sourceDataLine.stop();
                }
            }

        } catch (UnsupportedAudioFileException
                | LineUnavailableException
                | IOException e) {
            throw new IllegalStateException(e);
        }
    }

/*

    public void playWav(InputStream soundInputStream) throws IOException {
        // create an audiostream from the inputstream
        AudioStream audioStream = new AudioStream(soundInputStream);
        // play the audio clip with the audioplayer class
        AudioPlayer.player.start(audioStream);
    }

*/

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine sourceDataLine) throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            sourceDataLine.write(buffer, 0, n);
        }
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
        playSound.play(args[0]);
    }
}