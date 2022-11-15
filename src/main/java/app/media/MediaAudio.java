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
    private double defaultVolume;



    public MediaAudio(long id, String name, Set<String> tags, double x, double y,
                      double width, double height, double angle, int zIndex, byte[] rawData,
                      ArrayList<Duration>  timestamps, double defaultVolume) {
        super(id, name, tags, x, y, width, height, angle, zIndex, rawData);
        this.timestamps = timestamps;
        this.defaultVolume = defaultVolume;
    }


    public ArrayList<Duration>  getTimestamps() {
        return timestamps;
    }

    public double getDefaultVolume() {
        return defaultVolume;
    }

    public void setTimestamps(ArrayList<Duration>  timestamps) {
        this.timestamps = timestamps;
    }

    public void setDefaultVolume(double defaultVolume) {
        this.defaultVolume = defaultVolume;
    }
}