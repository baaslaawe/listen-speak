package org.snitko.app.listenspeak.listen;


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Recognizer {

    private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "resource:/edu/cmu/sphinx/demo/dialog/";

    private LiveSpeechRecognizer sampleRecognizer;
    private String stopCommand;


    public Recognizer() throws IOException {
        this(false, null);
    }

    public Recognizer(String grammarFileName) throws IOException {
        this(true, grammarFileName);
    }

    public Recognizer(boolean useGrammar, String grammarFileName) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);

        configuration.setUseGrammar(useGrammar);
        if (grammarFileName != null) {
            configuration.setGrammarPath(GRAMMAR_PATH);
            configuration.setGrammarName(grammarFileName);
        }
        sampleRecognizer = new LiveSpeechRecognizer(configuration);

        stopCommand = "exit";
    }


    public List<String> listenFor(String... keywordToMatch) {
        List<String> keywordToMatchList = Arrays.asList(keywordToMatch);
        boolean keepListening = true;
        List<String> matchedResult = new ArrayList<String>();
        while (keepListening) {
            sampleRecognizer.startRecognition(true);
            System.out.println("+++++=====+++++=====+++++== START LISTENNIG ==+++++=====+++++=====+++++");
            SpeechResult result = sampleRecognizer.getResult();
            sampleRecognizer.stopRecognition();
            System.out.println("+++++=====+++++=====+++++== END LISTENNIG ==+++++=====+++++=====+++++");

            String utterance = result.getHypothesis();
            matchedResult.add(utterance);

            System.out.println("---> I heard: " + utterance);

            if (stopCommand.equals(utterance) || keywordToMatchList.contains(utterance))
                keepListening = false;
        }

        return matchedResult;
    }


    public void setStopCommand(String stopCommand) {
        this.stopCommand = stopCommand;
    }
}