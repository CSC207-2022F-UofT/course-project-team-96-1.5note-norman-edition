package app.media;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Set;

public class MediaAudio extends FileMedia{
    //A subclass of PageFileMedia, defining Audio that exists on the page
    //Instance Attributes:
    //  timestamps: A set of timestamps associated with this audio
    //  defaultVolume: default volume of the audio as defined by the audio files
    private ArrayList<Duration> timestamps;



    public MediaAudio(String name, double x, double y, double width, double height, byte[] rawData,
                      ArrayList<Duration>  timestamps) {
        super(name, x, y, width, height, rawData);
        this.timestamps = timestamps;
    }


    public ArrayList<Duration>  getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(ArrayList<Duration>  timestamps) {
        this.timestamps = timestamps;
    }
}