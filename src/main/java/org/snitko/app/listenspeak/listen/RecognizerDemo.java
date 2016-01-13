package org.snitko.app.listenspeak.listen;


import java.util.ArrayList;
import java.util.List;


public class RecognizerDemo {
    public static void main(String[] args) throws Exception {
        //Recognizer recognizer = new Recognizer();
        Recognizer recognizer = new Recognizer(true, "custom.grxml");

        recognizer.setStopCommand("lalalalala");

        System.out.println("Say alexa, foo, bar or lalalalala for exit");

        List<String> matchedKeywords = new ArrayList<String>();

        String whatWeWantToHear = "alexa";
        while (!matchedKeywords.contains(whatWeWantToHear)) {
            matchedKeywords = recognizer.listenFor(whatWeWantToHear);
            System.out.println("WE HEARD SO FAR ------> " + matchedKeywords);
        }
        System.out.println("HEARD \"" + whatWeWantToHear + "\" - BOOOYYAAAAAA");
    }
}