import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;

public class MediaAudio extends PageFileMedia{
    //A subclass of PageFileMedia, defining Audio that exists on the page
    //Instance Attributes:
    //  -audio: The GUI representation of the audio in question
    //  -timestamps: A set of timestamps that can be clicked associated with this audio
    private MediaPlayer audio;
    private ArrayList<MediaHyperlink> timestamps;

    public MediaAudio(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag,
                      MediaPlayer audio, byte[] rawData, ArrayList<MediaHyperlink>  timestamps) {
        super(position, dimensions, angle, zIndex, id, tag, rawData);
        this.audio = audio;
        this.timestamps = timestamps;
    }

    public MediaPlayer getAudio() {
        return audio;
    }

    public ArrayList<MediaHyperlink>  getTimestamps() {
        return timestamps;
    }

    public void setAudio(MediaPlayer audio) {
        this.audio = audio;
    }

    public void setTimestamps(ArrayList<MediaHyperlink>  timestamps) {
        this.timestamps = timestamps;
    }
}