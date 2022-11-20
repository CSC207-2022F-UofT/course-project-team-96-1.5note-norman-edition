package app.media_managers;
import app.MediaCommunicator;
import app.MediaObserver;
import app.media.Media;
import app.media.MediaAudio;
import gui.error_window.ErrorWindow;
import gui.media.GUIAudio;
import gui.page.Page;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;


public class AudioModifier implements MediaManager {
    /**
    * Manages creation/interactions on MediaAudio based on Menubars/Toolbars
    * Instance Attributes:
     * - timestamp: used to add a timestamp to an audio
     * - audio: audio to be modified
     * - page: The page where the audio exists/will exist on
    */

    private Duration timestamp;
    private MediaAudio audio;
    private MediaCommunicator communicator;

    @Override
    public void addMedia() throws Exception{
        //Loading raw audio data based on user selection
        Storage fileManager = new FileLoaderWriter();
        HashMap<String, byte[]> fileData = fileManager.readFile(new String[]{"*.mp3","*.wav"},
                "Audio (.mp3, .wav)");
        if (fileData != null)    {
            String fileName = (String) (fileData.keySet().toArray())[0];
            MediaAudio audio = new MediaAudio(fileName, 200, 200, 200, 200,
                    fileData.get(fileName), new ArrayList<Duration>()); //Temp Constructor

            //Giving the audio an ID then adding it to the page
            this.communicator.updateMedia(audio);
            }
    }

    @Override
    public void modifyMedia() throws Exception{
        //Either adds or removes timestamps from the audio
        if (audio.getTimestamps().contains(timestamp))  {
            audio.getTimestamps().remove(timestamp);
        } else {
            audio.getTimestamps().add(timestamp);
        }
        communicator.updateMedia(audio);
    }

    @Override
    public void searchMedia() {

    }

    public void addTimeStamp(Duration givenTimeStamp){

        this.timestamp = givenTimeStamp;
    }

    public void setAudio(MediaAudio audio) {
        this.audio = audio;
    }

    public void setCommunicator(MediaCommunicator communicator) {
        this.communicator = communicator;
    }
}
