package org.snitko.app.listen.demo;


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import java.util.HashMap;
import java.util.Map;


public class KeywordsDemo {

    private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "resource:/edu/cmu/sphinx/demo/dialog/";

    private static final Map<String, Integer> DIGITS = new HashMap<String, Integer>();

    static {
        DIGITS.put("oh", 0);
        DIGITS.put("zero", 0);
        DIGITS.put("one", 1);
        DIGITS.put("two", 2);
        DIGITS.put("three", 3);
        DIGITS.put("four", 4);
        DIGITS.put("five", 5);
        DIGITS.put("six", 6);
        DIGITS.put("seven", 7);
        DIGITS.put("eight", 8);
        DIGITS.put("nine", 9);
    }

    private static double parseNumber(String[] tokens) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < tokens.length; ++i) {
            if (tokens[i].equals("point"))
                sb.append(".");
            else
                sb.append(DIGITS.get(tokens[i]));
        }

        return Double.parseDouble(sb.toString());
    }


    private static void recognizerSample(LiveSpeechRecognizer recognizer) {
        System.out.println("SAMPLE recognition (using GrXML)");
        System.out.println("--------------------------------");
        System.out.println("Example: foo, bar, do, change");
        System.out.println("--------------------------------");

        recognizer.startRecognition(true);
        while (true) {
            String utterance = recognizer.getResult().getHypothesis();
            if (utterance.equals("change"))
                break;
            else
                System.out.println("You said: " + utterance);
        }
        recognizer.stopRecognition();
    }


    private static void recognizeDigits(LiveSpeechRecognizer recognizer) {
        System.out.println("Digits recognition (using GrXML)");
        System.out.println("--------------------------------");
        System.out.println("Example: one two three");
        System.out.println("Say \"101\" to exit");
        System.out.println("--------------------------------");

        recognizer.startRecognition(true);
        while (true) {
            String utterance = recognizer.getResult().getHypothesis();
            if (utterance.equals("one zero one") || utterance.equals("one oh one"))
                break;
            else
                System.out.println("You said: " + utterance);
        }
        recognizer.stopRecognition();
    }

    private static void recognizerBankAccount(LiveSpeechRecognizer recognizer) {
        System.out.println("This is bank account voice menu");
        System.out.println("-------------------------------");
        System.out.println("Example: balance");
        System.out.println("Example: withdraw zero point five");
        System.out.println("Example: deposit one two three");
        System.out.println("Example: back");
        System.out.println("-------------------------------");

        double savings = .0;
        recognizer.startRecognition(true);

        while (true) {
            String utterance = recognizer.getResult().getHypothesis();
            if (utterance.endsWith("back")) {
                break;
            } else if (utterance.startsWith("deposit")) {
                double deposit = parseNumber(utterance.split("\\s"));
                savings += deposit;
                System.out.format("Deposited: $%.2f\n", deposit);
            } else if (utterance.startsWith("withdraw")) {
                double withdraw = parseNumber(utterance.split("\\s"));
                savings -= withdraw;
                System.out.format("Withdrawn: $%.2f\n", withdraw);
            } else if (!utterance.endsWith("balance")) {
                System.out.println("Unrecognized command: " + utterance);
            }

            System.out.format("Your savings: $%.2f\n", savings);
        }

        recognizer.stopRecognition();
    }


    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);

        configuration.setGrammarName("dialog");
        LiveSpeechRecognizer liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);

        configuration.setGrammarName("digits.grxml");
        LiveSpeechRecognizer grxmlRecognizer = new LiveSpeechRecognizer(configuration);

        configuration.setGrammarName("sample.grxml");
        LiveSpeechRecognizer sampleRecognizer = new LiveSpeechRecognizer(configuration);


        liveSpeechRecognizer.startRecognition(true);
        while (true) {
            System.out.println("Choose menu item:");
            System.out.println("Example: go to the bank account");
            System.out.println("Example: exit the program");
            System.out.println("Example: weather forecast");
            System.out.println("Example: digits\n");

            String utterance = liveSpeechRecognizer.getResult().getHypothesis();
            System.out.println("---> UTTERANCE IS: " + utterance);


            if (utterance.startsWith("exit"))
                break;

            if (utterance.equals("digits")) {
                liveSpeechRecognizer.stopRecognition();
                recognizeDigits(grxmlRecognizer);
                liveSpeechRecognizer.startRecognition(true);
            }

            if (utterance.equals("bank account")) {
                liveSpeechRecognizer.stopRecognition();
                recognizerBankAccount(liveSpeechRecognizer);
                liveSpeechRecognizer.startRecognition(true);
            }

            if (utterance.equals("sample")) {
                liveSpeechRecognizer.stopRecognition();
                recognizerSample( sampleRecognizer);
                liveSpeechRecognizer.startRecognition(true);
            }

        }

        liveSpeechRecognizer.stopRecognition();
    }
}