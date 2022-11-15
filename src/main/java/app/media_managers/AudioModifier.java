package app.media_managers;

import app.media_managers.MediaManager;

public class AudioModifier implements MediaManager {

    private String TimeStamp;

    @Override
    public void addMedia() {

    }

    @Override
    public void modifyMedia() {

    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(String givenTimeStamp){

        this.TimeStamp = givenTimeStamp;
    }

}
