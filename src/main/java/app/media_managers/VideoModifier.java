package app.media_managers;

import app.MediaCommunicator;
import app.media.MediaVideo;
import javafx.util.Duration;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoModifier extends AudioModifier{

    @Override
    public void addMedia(MediaCommunicator communicator, double x, double y) throws Exception{
        //Loading raw audio data based on user selection
        Storage fileManager = new FileLoaderWriter();

        HashMap<String, byte[]> fileData = fileManager.readFile(new String[]{"*.mp4"}, "Video(*.mp4)");
        if (fileData != null)    {
            String fileName = (String) (fileData.keySet().toArray())[0];
            MediaVideo video = new MediaVideo(fileName.substring(0, fileName.length() - 4),
                    x - 100, y - 100, 200, 200, fileData.get(fileName), new ArrayList<Duration>());
            //Giving the audio an ID then adding it to the page
            communicator.updateMedia(video);
        }
    }
}
