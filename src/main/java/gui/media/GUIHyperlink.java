package gui.media;
import javafx.scene.control.Hyperlink;
import app.media.MediaHyperlink;

public class GUIHyperlink extends GUIMedia<MediaHyperlink>{
    /**
     * Instance Attributes:
     *  -hyperlink: The Hyperlink object represented by media
    **/

    private final Hyperlink hyperlink;

    public GUIHyperlink(MediaHyperlink media) {
        super(media);
        this.hyperlink = new Hyperlink(media.getText());

        this.getChildren().add(hyperlink);
    }

    public Hyperlink getHyperlink() {
        return hyperlink;
    }

    @Override
    public String toString()    {
        return this.getMedia().getText();
    }
}
