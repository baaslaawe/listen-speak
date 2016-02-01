package org.snitko.app.record;

/**
 Copyright [2015] vadim

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created: 1/31/16
 */


import org.apache.commons.io.FileUtils;

import javax.sound.sampled.*;
import java.io.*;

/**
 * * A sample program is to demonstrate how to record sound in Java
 * * author: www.codejava.net
 */
public class SoundRecorder {
    /**
     * Defines an audio format.  Using standard telephony format for now
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
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
        AudioInputStream ais = new AudioInputStream(targetDataLine);
        System.out.println("Start recording...");

        // start recording
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, waveFile);
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
            if (LineEvent.Type.START.equals(event.getType()))
            {
                System.out.println("Started Recording.  Message from the listener.");
                try {
                    Thread.sleep(durationInSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println("Stopped Recording.  Message from the listener.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    System.out.println("Finished");
                }

            }
        }
    }

    /**
     * Entry to run the program
     */


    public static void main(String[] args) throws Exception {
        final SoundRecorder recorder = new SoundRecorder();

        // path of the wav file
        File wavFile = FileUtils.getFile(FileUtils.getUserDirectory(), "foo.wav");
        System.out.println("File Path: " + wavFile.getAbsolutePath());
        // start recording
        recorder.record(wavFile, 3);
    }
}