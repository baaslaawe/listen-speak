package org.snitko.app.speak;

public class TTSDemo {
    public static void main(String[] args) {
        TTS demo = new TTS();
        demo.speak("test");
        demo.speak("banana");
        demo.speak("howdy");
    }
}
