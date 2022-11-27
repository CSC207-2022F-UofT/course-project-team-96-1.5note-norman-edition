package app.media_managers;

import app.media.MediaHyperlink;
import app.media_managers.MediaManager;

public class TextModifier implements MediaManager {
    private String link;
    @Override
    public void addMedia() {

    }

    @Override
    public void modifyMedia() {

    }

    @Override
    public void searchMedia() {

    }

    public MediaHyperlink createHyperlink(String text, String source) {   //temp method
        return new MediaHyperlink("", 0, 0, 0, 0, text, source);
    }

    public void addLink(String givenLink){
        this.link = givenLink;
    }


}
