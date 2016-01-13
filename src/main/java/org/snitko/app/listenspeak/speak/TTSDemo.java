package org.snitko.app.listenspeak.speak;

public class TTSDemo {
    public static void main(String[] args) {
        TTS demo = new TTS();
        demo.speak("test");
        demo.speak("banana");
        demo.speak("howdy");
    }
}
