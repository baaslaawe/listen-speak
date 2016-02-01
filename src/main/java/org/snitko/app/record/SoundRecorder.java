package org.snitko.app.record;

/**
 * Created by vadim on 1/31/16.
 */


import org.apache.commons.io.FileUtils;

import javax.sound.sampled.*;
import java.io.*;

/**
 * * A sample program is to demonstrate how to record sound in Java
 * * author: www.codejava.net
 */
public class SoundRecorder {
    // the targetDataLine from which audio data is captured
    private TargetDataLine targetDataLine;

    /**
     * Defines an audio format
     */


    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }


    /**
     * Captures the sound and record into a WAV file
     */


    public void start(File waveFile) throws LineUnavailableException, IOException {

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixer : mixers) {
            System.out.println("mixers are: " + mixer.toString());
        }

        AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();
        for (AudioFileFormat.Type type : types) {
            System.out.println("audio types are: " + type.toString());
        }

        AudioFormat audioFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        // checks if system supports the data targetDataLine
        if (!AudioSystem.isLineSupported(dataLineInfo)) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
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
    public void finish() {
        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("Finished");
    }


    /**
     * Entry to run the program
     */


    public static void main(String[] args) throws Exception {
        final SoundRecorder recorder = new SoundRecorder();
        final long recordTimeInMillie = 5 * 1000;

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(recordTimeInMillie);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }


        });

        stopper.start();

        // path of the wav file
        File wavFile = FileUtils.getFile(FileUtils.getUserDirectory(), "foo.wav");
        System.out.println("File Path: " + wavFile.getAbsolutePath());
        // start recording
        recorder.start(wavFile);
    }
}