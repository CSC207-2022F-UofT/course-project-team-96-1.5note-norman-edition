import javafx.scene.media.MediaPlayer;

public class MediaAudio extends PageMedia{
    private MediaPlayer audio;
    private MediaText[] timestamps;

    public MediaAudio(double[] position, double[] dimensions, double angle, int zIndex, String name, String tag,
                      MediaPlayer audio, byte[] rawData, MediaText[] timestamps) {
        super(position, dimensions, angle, zIndex, name, tag, rawData);
        this.audio = audio;
        this.timestamps = timestamps;
    }

    public MediaPlayer getAudio() {
        return audio;
    }

    public MediaText[] getTimestamps() {
        return timestamps;
    }

    public void setAudio(MediaPlayer audio) {
        this.audio = audio;
    }

    public void setTimestamps(MediaText[] timestamps) {
        this.timestamps = timestamps;
    }
}