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

    public Hyperlink getHyperlink() {
        return hyperlink;
    }
}
