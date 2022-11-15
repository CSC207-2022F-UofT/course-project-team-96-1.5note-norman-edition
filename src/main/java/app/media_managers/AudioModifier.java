package app.media_managers;
import app.MediaCommunicator;
import app.media.MediaAudio;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class AudioModifier implements MediaManager {

    private Duration TimeStamp;
    private MediaAudio audio;
    private Page page;

    @Override
    public void addMedia() {

        //Loading raw audio data based on user selection
        Storage fileManager = new FileLoaderWriter();
        byte[] rawData = fileManager.readFile(new String[]{"*.mp3","*.wav","*.ogg"});

        try {
            Long id = page.getCommunicator().getNewID();
            fileManager.writeFile("temp/", rawData); //Creating temp file for use by javafx.Media Class

            MediaAudio audio = new MediaAudio(id, "", new HashSet<String>(), 0, 0, 0, 0,
                    0, 0, rawData, new ArrayList<Duration>(), 0); //Temp Constructor
            GUIAudio audioGUI = new GUIAudio(audio, "temp/id"+ id +".file");
            this.page.addMedia(audioGUI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyMedia() {
        this.audio.getTimestamps().add(TimeStamp);
        try {
            this.page.getCommunicator().updateMedia(audio);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(Duration givenTimeStamp){

        this.TimeStamp = givenTimeStamp;
    }

    public void setAudio(MediaAudio audio) {
        this.audio = audio;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
