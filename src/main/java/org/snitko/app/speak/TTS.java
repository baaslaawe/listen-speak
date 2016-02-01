package org.snitko.app.speak;
/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) [2015] [Vadim Snitkovsky]
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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


    public static void main(String[] args) {
        TTS demo = new TTS();
        demo.speak("test");
        demo.speak("banana");
        demo.speak("howdy");
    }
}
