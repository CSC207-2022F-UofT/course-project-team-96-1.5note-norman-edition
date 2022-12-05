package gui.media;
import app.media.Media;
//import app.media.TextBox;
import app.media.Hyperlink;
import app.media.MediaText;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class GUIHyperlinkBox extends GUIMedia<Hyperlink> {
    private Text text;
    private String link;
    private Color colour;


    protected void setInitialValues() {
        this.text = new Text("");
        colour = Color.BLACK;

        getChildren().clear();
        getChildren().addAll(this.text);

        this.link = new String ("");
        getTextNode().setUnderline(true);
    }

    public GUIHyperlinkBox(Point2D point, String text, String link, Color colour) {
        super(new Hyperlink(point.getX(), point.getY(), text, link, colour.toString()));

        setInitialValues();
        this.colour = colour;
        setText(text);
        setLink(link);
    }

    public GUIHyperlinkBox(Hyperlink media) {
        super(media);
        mediaUpdated(media);
    }

// clicking links doesn't work when this method is uncommented
/*    @Override
    public void mediaUpdated(Media media) {
        Hyperlink newHyperlink = (Hyperlink) media;

        setInitialValues();
        setMedia(newHyperlink);
        colour = Color.valueOf(newHyperlink.getColour());
        setText(newHyperlink.getText());
    }*/


    public String getLink(){
        return this.link;
    }

    public void updateHyperLink(String textIn, String givenLink) {
        this.text.setText(textIn);
        this.link = givenLink;
        this.getMedia().setText(textIn);
    }


    protected Text getTextNode() {
        return text;
    }

    protected void setText(String textIn) {
        this.text.setFill(colour);
        this.text.setText(textIn);
        this.getMedia().setText(textIn);
    }

    protected void setLink(String linkIn) {
        this.link = linkIn;
        this.getMedia().setText(linkIn);
    }
    public String getText() { return this.text.getText(); }

    public void end() {
        if (getMedia().getText().equals(this.text.getText())) {
            getMedia().setText(this.text.getText());
        }
    }

}
