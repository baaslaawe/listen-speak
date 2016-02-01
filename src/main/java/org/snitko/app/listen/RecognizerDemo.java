package org.snitko.app.listen;


import java.util.ArrayList;
import java.util.List;


public class RecognizerDemo {
    public static void main(String[] args) throws Exception {
        String whatWeWantToHear = "alexa";


        Recognizer recognizer = new Recognizer("custom.grxml");
        recognizer.setStopCommand("lambda");

        System.out.println("Say alexa or lambda for exit");

        List<String> matchedKeywords = new ArrayList<String>();

        while (!matchedKeywords.contains(whatWeWantToHear)) {
            matchedKeywords = recognizer.listenLive(whatWeWantToHear);
            System.out.println("WE HEARD SO FAR ------> " + matchedKeywords);
        }
        System.out.println("HEARD \"" + whatWeWantToHear + "\" - BOOOYYAAAAAA");
    }
}