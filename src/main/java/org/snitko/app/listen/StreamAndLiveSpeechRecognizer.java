package org.snitko.app.listen;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Microphone;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.IOException;
import java.io.InputStream;


public class StreamAndLiveSpeechRecognizer{// extends AbstractSpeechRecognizer {

    /*
    private boolean useMicrophone;
    private Microphone microphone;

    public StreamAndLiveSpeechRecognizer(Configuration configuration, boolean useMicrophone) throws IOException {
        super(configuration);
        this.useMicrophone = useMicrophone;
        
        if (useMicrophone) {
            this.microphone = this.speechSourceProvider.getMicrophone();
            ((StreamDataSource) this.context.getInstance(StreamDataSource.class)).setInputStream(this.microphone.getStream());
        }
    }

    public void startRecognition() {
        this.recognizer.allocate();
        if (useMicrophone)
            this.microphone.startRecording();
    }

    public void startRecognition(InputStream stream) {
        this.startRecognition(stream, TimeFrame.INFINITE);
    }

    public void startRecognition(InputStream stream, TimeFrame timeFrame) {
        this.recognizer.allocate();
        this.context.setSpeechSource(stream, timeFrame);
    }

    public void stopRecognition() {
        if (useMicrophone)
            this.microphone.stopRecording();
        this.recognizer.deallocate();
    }
    */
}
