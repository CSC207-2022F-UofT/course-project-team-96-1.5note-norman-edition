import javafx.scene.*;

public class MediaAudio extends PageMedia{
    private PageMedia audio;
    private MediaText[] timestamps; // TODO: Temp object type, feel free to change as needed.

    public MediaAudio(double[] position, double[] dimensions, double angle, int zIndex, String name, String tag,
                      PageMedia audio, byte[] rawData, MediaText[] timestamps) {
        super(position, dimensions, angle, zIndex, name, tag, rawData);
        this.audio = audio;
        this.timestamps = timestamps;
    }

    public PageMedia getAudio() {
        return audio;
    }

    public MediaText[] getTimestamps() {
        return timestamps;
    }

    public void setAudio(PageMedia audio) {
        this.audio = audio;
    }

    public void setTimestamps(MediaText[] timestamps) {
        this.timestamps = timestamps;
    }
}
