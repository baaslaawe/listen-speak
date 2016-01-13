package org.snitko.app.listenspeak.speak;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


public class TTS {

    private VoiceManager voiceManager = VoiceManager.getInstance();
    private String voiceConfig;

    public TTS() {
        this("kevin16");
    }

    public TTS(String voiceConfig) {
        this.voiceConfig = voiceConfig;
    }

    public boolean speak(String text) {
        Voice voice = voiceManager.getVoice(voiceConfig);
        try {
            voice.allocate();
            return voice.speak(text);
        } finally {
            voice.deallocate();
        }
    }
}
