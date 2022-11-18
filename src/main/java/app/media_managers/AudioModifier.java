package app.media_managers;
import app.media.MediaAudio;
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
    private Page page;

    @Override
    public void addMedia() {
        //Loading raw audio data based on user selection
        Storage fileManager = new FileLoaderWriter();
        byte[] rawData = fileManager.readFile(new String[]{"*.mp3","*.wav"}, "Audio (.mp3, .wav)");

        try {
            MediaAudio audio = new MediaAudio("", 200, 200, 200, 200, rawData, new ArrayList<Duration>(),
                    0); //Temp Constructor
            GUIAudio audioGUI = new GUIAudio(audio);

            //Giving the audio an ID then adding it to the page
            this.page.updateMedia(audioGUI);
            this.page.addMedia(audioGUI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyMedia() {
        this.audio.getMedia().getTimestamps().add(timestamp);
        this.page.updateMedia(this.audio);
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

    public void setPage(Page page) {
        this.page = page;
    }
}
