package app.media_managers;

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

    public void addLink(String givenLink){
        this.link = givenLink;
    }


}
