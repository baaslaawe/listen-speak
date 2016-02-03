package org.snitko.app.record;

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


import org.apache.commons.io.FileUtils;

import javax.sound.sampled.*;
import java.io.*;


public class SoundRecorder {
    /**
     * Defines an audio format.  Using standard telephony format for now
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }


    /**
     * Captures the sound and record into a WAV file
     */


    public void record(File waveFile, final long durationInSeconds) throws LineUnavailableException, IOException {
        AudioInputStream audioInputStream = record(durationInSeconds);
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, waveFile);
    }

    public AudioInputStream record(long durationInSeconds) throws LineUnavailableException {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixer : mixers) {
            System.out.printf("mixer name/desc/vendor/version: %s, %s, %s, %s\n", mixer.getName(), mixer.getDescription(), mixer.getVendor(), mixer.getVersion());
        }

        AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();
        for (AudioFileFormat.Type type : types) {
            System.out.println("audio types are: " + type.toString());
        }

        AudioFormat audioFormat = getAudioFormat();

        // the targetDataLine from which audio data is captured
        TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
        System.out.println("targetDataLine: " + targetDataLine + "\n\tlineInfo" + targetDataLine.getLineInfo() + "\n\tformat: " + targetDataLine.getFormat());


        // checks if system supports the data targetDataLine
        if (!AudioSystem.isLineSupported(targetDataLine.getLineInfo())) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        targetDataLine.addLineListener(new TimedLineListener(targetDataLine, durationInSeconds));
        targetDataLine.open(audioFormat);
        targetDataLine.start(); // start capturing

        System.out.println("Start capturing...");
        AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
        System.out.println("Start recording...");
        return audioInputStream;
    }


    /**
     * Closes the target data targetDataLine to finish capturing and recording
     */
    private class TimedLineListener implements LineListener {

        private long durationInSeconds;
        private TargetDataLine targetDataLine;

        public TimedLineListener(TargetDataLine targetDataLine, long durationInSeconds) {
            this.durationInSeconds = durationInSeconds;
            this.targetDataLine = targetDataLine;
        }

        public void update(LineEvent event) {
            if (LineEvent.Type.START.equals(event.getType())) {
                System.out.println("Started Recording.  Message from the listener.");
                try {
                    Thread.sleep(durationInSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Stopped Recording.  Message from the listener.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    System.out.println("Finished. Message from the listener.");
                }

            }
        }
    }

    /**
     * Entry to run the program
     */


    public static void main(String[] args) throws Exception {
        SoundRecorder recorder = new SoundRecorder();
        // path of the wav file
        File wavFile = FileUtils.getFile(FileUtils.getUserDirectory(), "foo.wav");
        System.out.println("File Path: " + wavFile.getAbsolutePath());
        // start recording
        recorder.record(wavFile, 4);
    }
}