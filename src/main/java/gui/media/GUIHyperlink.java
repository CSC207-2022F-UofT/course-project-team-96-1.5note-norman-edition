package gui.media;
import javafx.scene.control.Hyperlink;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import app.media.MediaHyperlink;

public class GUIHyperlink extends GUIMedia<MediaHyperlink>{
    /**
     * Instance Attributes:
     *  -hyperlink: The Hyperlink object represented by media
    **/

    private Hyperlink hyperlink;

    public GUIHyperlink(MediaHyperlink media) {
        super(media);
        this.hyperlink = new Hyperlink(media.getText());
    }

    public void createTimestampLink(MediaPlayer audioPlayer)  {
        //Uses the media's source attribute to create a timestamp
        //Precondition: media.link is a double representing the duration in milliseconds of the associated GUIAudio
        Duration length = new Duration(Double.parseDouble(this.getMedia().getLink()));
        this.hyperlink.setOnAction(e -> {
            audioPlayer.seek(length);
            audioPlayer.play();
        });

    }
}
