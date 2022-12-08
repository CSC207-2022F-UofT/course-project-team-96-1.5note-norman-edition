package app.media;
import javafx.util.Duration;
import java.util.ArrayList;

public class MediaAudio extends FileMedia{
    /** Entity class for storing audio
     * <p>
     * Aside from regular Media and FileMedia parameters, defined by a list of Durations denoting points to be linked
     * to within the associated playable type
     * <p>
     * Representation Invariant: All Durations in timestamps are valid points in the associated media
     */

    private final ArrayList<Duration> timestamps;

    public MediaAudio(String name, double x, double y, double width, double height, byte[] rawData,
                      ArrayList<Duration>  timestamps) {
        super(name, x, y, width, height, rawData);
        this.timestamps = timestamps;
    }


    public ArrayList<Duration>  getTimestamps() {
        return timestamps;
    }

}