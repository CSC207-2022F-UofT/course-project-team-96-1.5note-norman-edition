package app.media_managers;
import app.MediaObserver;
import app.media.MediaAudio;
import gui.error_window.ErrorWindow;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.ArrayList;



public class AudioModifier implements MediaManager {
    /**
    * Manages creation/interactions on MediaAudio based on Menubars/Toolbars
    * Instance Attributes:
     * - timestamp: used to add a timestamp to an audio
     * - audio: audio to be modified
     * - page: The page where the audio exists/will exist on
    */

    private Duration timestamp;
    private GUIAudio audio;
    private MediaObserver page;

    @Override
    public void addMedia() throws Exception{
        //Loading raw audio data based on user selection
        Storage fileManager = new FileLoaderWriter();
            byte[] rawData = fileManager.readFile(new String[]{"*.mp3","*.wav"}, "Audio (.mp3, .wav)");
            if (rawData != null)    {
                MediaAudio audio = new MediaAudio("", 200, 200, 200, 200, rawData,
                        new ArrayList<Duration>()); //Temp Constructor

                //Giving the audio an ID then adding it to the page
                this.page.mediaUpdated(audio);
            }
    }

    @Override
    public void modifyMedia() throws Exception {
        this.audio.getMedia().getTimestamps().add(timestamp);
        this.page.mediaUpdated(this.audio.getMedia());
    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(Duration givenTimeStamp){

        this.timestamp = givenTimeStamp;
    }

    public void setAudio(GUIAudio audio) {
        this.audio = audio;
    }

    public void setPage(MediaObserver page) {
        this.page = page;
    }
}
