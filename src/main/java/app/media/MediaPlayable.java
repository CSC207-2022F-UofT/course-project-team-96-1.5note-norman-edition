package app.media;
import javafx.util.Duration;
import java.util.ArrayList;

public class MediaPlayable extends FileMedia{
    /** Entity class for storing playable types of media
     * <p>
     * Aside from regular Media and FileMedia parameters, defined by a list of Durations denoting points to be linked
     * to within the associated playable type, and a String defining the type of this playabale
     * <p>
     * Representation Invariant: All Durations in timestamps are valid points in the associated playable media
     */

    private final ArrayList<Duration> timestamps;
    private final String type;

    public MediaPlayable(String name, double x, double y, double width, double height, byte[] rawData,
                         ArrayList<Duration>  timestamps, String type) {
        super(name, x, y, width, height, rawData);
        this.timestamps = timestamps;
        this.type = type;
    }


    public ArrayList<Duration>  getTimestamps() {
        return timestamps;
    }

    public String getType() {
        return type;
    }
}