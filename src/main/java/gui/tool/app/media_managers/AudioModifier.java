package gui.tool.app.media_managers;
import gui.tool.app.MediaCommunicator;
import gui.tool.app.media.MediaAudio;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;


public class AudioModifier {
    /**
    * Manages creation/interactions on MediaAudio
     * <p>
     * Class stores instances of a MediaAudio to modify, the MediaCommunicator of the page it is on, and may store
     * a Duration that should be added or removed from instance MediaAudio
    */

    private Duration timestamp;
    private MediaAudio audio;
    private MediaCommunicator communicator;

    /** Allows the user to select an audio file to add to the page
     * @throws Exception when user selected file fails to load
     */
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

    /**
     * Allowing timestamps to either be added or removed from referenced parameter audio
     * <p>
     * If this class' timestamp attribute already exist in the audio attribute, it is removed from audio. Otherwise,
     * it is added
     * @throws Exception when MediaCommunicator fails to update referenced audio
     */
    public void modifyMedia() throws Exception{
        //Either adds or removes timestamps from the audio
        if (audio.getTimestamps().contains(timestamp))  {
            audio.getTimestamps().remove(timestamp);
        } else {
            audio.getTimestamps().add(timestamp);
        }
        communicator.updateMedia(audio);
    }

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
