package org.snitko.app.listen;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;


public class LiveRecognizerTest {

    @Test
    public
    void testLanguageModel() throws IOException {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        InputStream stream = LiveRecognizerTest.class.getResourceAsStream("/edu/cmu/sphinx/tools/bandwidth/10001-90210-01803.wav");
        stream.skip(44);

        // Simple recognition with generic model
        recognizer.startRecognition(stream);
        SpeechResult result = recognizer.getResult();
        recognizer.stopRecognition();

        assertEquals("one zero zero zero one", result.getHypothesis());

        WordResult word = result.getWords().get(0);
        assertEquals("{what, 0.776, [820:1080]}", word.toString());
    }





    @Test
    public void testGrammar() throws IOException {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setGrammarPath("resource:/edu/cmu/sphinx/jsgf/test/");
        configuration.setGrammarName("digits.grxml");
        configuration.setUseGrammar(true);

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        InputStream stream = LiveRecognizerTest.class
                .getResourceAsStream("/edu/cmu/sphinx/tools/bandwidth/10001-90210-01803.wav");
        stream.skip(44);

        // Simple recognition with generic model
        recognizer.startRecognition(stream);
        SpeechResult result = recognizer.getResult();

        assertEquals("one zero zero zero one", result.getHypothesis());

        WordResult word = result.getWords().get(0);
        assertEquals("{one, 1.000, [840:1060]}", word.toString());

        assertEquals("one", word.getWord().toString());
    }
}
