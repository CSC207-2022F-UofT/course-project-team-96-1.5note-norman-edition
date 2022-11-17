package gui.media;

import app.media.Media;
import app.media.TextBox;
import javafx.geometry.Point2D;
import app.media.HyperlinkBox;

import javafx.scene.control.Hyperlink;
import javafx.scene.text.*;

public class GUIHyperlinkBox extends GUITextBox{
    private Text text;
    private String link;

    private void setInitialValues() {
        this.text = new Text("");

        getChildren().clear();
        getChildren().addAll(this.text);
    }


    public GUIHyperlinkBox(Point2D point, String text, String link) {
        super(point, text);
        this.link = link;
        System.out.print(this.link);
    }

    public GUIHyperlinkBox(TextBox media) {
        super(media);
        mediaUpdated(media);
    }


}
