package app.media_managers;

import app.media.MediaHyperlink;
import app.media_managers.MediaManager;

public class TextModifier {
    private String link;

    //Special method since normally you add the associated GUIMedia class directly to the class, but in the
    //timestamp case it needs to be added under GUIAudio
    public MediaHyperlink createAudioTimestamp(String text, String source) {
        return new MediaHyperlink("", 0, 0, 0, 0, text, source); //temp constructor
    }

    public void addLink(String givenLink){
        this.link = givenLink;
    }


}
