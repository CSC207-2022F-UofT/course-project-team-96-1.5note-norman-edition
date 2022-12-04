package gui.media;
import app.media.Media;
//import app.media.TextBox;
import app.media.Hyperlink;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class GUIHyperlinkBox extends GUITextBox {
    //private Text text;
    private String link;

    @Override
    protected void setInitialValues() {
        super.setInitialValues();
        this.link = new String ("");
        getTextNode().setUnderline(true);
    }

    public GUIHyperlinkBox(Point2D point, String text, String link, Color colour) {
        super(new Hyperlink(point.getX(), point.getY(), text, link, colour.toString()));

        setInitialValues();
        setText(text);
        setLink(link);
    }

    public GUIHyperlinkBox(Hyperlink media) {
        super(media);
        mediaUpdated(media);
    }

    private void setLink(String givenLink) {
        this.link = givenLink;
    }

    public String getLink(){
        return this.link;
    }

    public void updateHyperLink(String textIn, String givenLink) {
        setText(textIn);
        this.link = givenLink;
    }

    public void end() {
        if (getMedia().getText().equals(getText())) {
            getMedia().setText(getText());
        }
    }

}
