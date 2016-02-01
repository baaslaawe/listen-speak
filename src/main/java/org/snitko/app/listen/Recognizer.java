package org.snitko.app.listen;


import edu.cmu.sphinx.api.*;
import edu.cmu.sphinx.result.WordResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Recognizer {

    private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "resource:/edu/cmu/sphinx/demo/dialog/";
    private static final String LANGUAGE_MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";


    private AbstractSpeechRecognizer sampleRecognizer;
    private boolean useGrammar;
    private String grammarFileName;
    private String stopCommand;

    public Recognizer() throws IOException {
        this(false, null);
    }

    public Recognizer(String grammarFileName) throws IOException {
        this(true, grammarFileName);
    }

    private Recognizer(boolean useGrammar, String grammarFileName) throws IOException {
        this.useGrammar = useGrammar;
        this.grammarFileName = grammarFileName;

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setUseGrammar(this.useGrammar);

        if (this.useGrammar && this.grammarFileName != null) {
            configuration.setGrammarPath(GRAMMAR_PATH);
            configuration.setGrammarName(this.grammarFileName);

            sampleRecognizer = new LiveSpeechRecognizer(configuration);
        } else {
            this.useGrammar = false;
            this.grammarFileName = null;
            configuration.setLanguageModelPath(LANGUAGE_MODEL_PATH);

            sampleRecognizer = new StreamSpeechRecognizer(configuration);
        }

        this.stopCommand = "exit";
    }


    public List<String> listenLive(String... keywordToMatch) {
        LiveSpeechRecognizer recognizer = ((LiveSpeechRecognizer)sampleRecognizer);
        String utterance = null;
        List<String> keywordToMatchList = Arrays.asList(keywordToMatch);
        List<String> matchedResult = new ArrayList<String>();



        while (!stopCommand.equals(utterance) && !keywordToMatchList.contains(utterance)) {
            recognizer.startRecognition(true);
            System.out.println("+++++=====+++++=====+++++== START LISTENNIG ==+++++=====+++++=====+++++");
            SpeechResult result = recognizer.getResult();
            recognizer.stopRecognition();
            System.out.println("+++++=====+++++=====+++++== END LISTENNIG ==+++++=====+++++=====+++++");

            utterance = result.getHypothesis();
            matchedResult.add(utterance);

            System.out.println("---> I heard: " + utterance);

        }

        return matchedResult;
    }


    public List<String> listenToStream(InputStream stream, String... keywordToMatch){
        StreamSpeechRecognizer recognizer = (StreamSpeechRecognizer)sampleRecognizer;
        String utterance = null;
        List<String> keywordToMatchList = Arrays.asList(keywordToMatch);
        List<String> matchedResult = new ArrayList<String>();



        while (!stopCommand.equals(utterance) && !keywordToMatchList.contains(utterance)) {
            recognizer.startRecognition(stream);
            System.out.println("+++++=====+++++=====+++++== START LISTENNIG ==+++++=====+++++=====+++++");
            SpeechResult result = recognizer.getResult();
            recognizer.stopRecognition();
            System.out.println("+++++=====+++++=====+++++== END LISTENNIG ==+++++=====+++++=====+++++");

            utterance = result.getHypothesis();
            matchedResult.add(utterance);

            System.out.println("---> I heard: " + utterance);

        }

        return matchedResult;
    }




    public void setStopCommand(String stopCommand) {
        this.stopCommand = stopCommand;
    }
}