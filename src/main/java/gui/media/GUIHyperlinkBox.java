package gui.media;
import app.media.Media;
import app.media.TextBox;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
public class GUIHyperlinkBox extends GUITextBox{
    private Text text;
    private String link;

    private void setInitialValues() {
        this.text = new Text("");
        this.link = new String ("");
        this.text.setUnderline(true);

        getChildren().clear();
        getChildren().addAll(this.text);
    }

    public GUIHyperlinkBox(Point2D point, String text, String link, Color colour ) {
        super(new TextBox(point.getX(), point.getY(), text, link, colour.toString()));

        setInitialValues();
        setText(text);
        setLink(link);
        this.text.setFill(colour);
    }
    @Override
    public void mediaUpdated(Media media) {
        TextBox newText = (TextBox) media;
        TextBox currentText = getMedia();

        if (currentText != newText) {
            setInitialValues();
            setMedia(newText);
            setText(newText.getText());
        }
    }

    public GUIHyperlinkBox(TextBox media) {
        super(media);
        mediaUpdated(media);
    }

    // copying methods from superclass because for some reason an error runs if these aren't here
    private void setText(String textIn) {
        this.text.setText(textIn); }

    private void setLink(String givenLink) {
        this.link = givenLink;
    }

    public String getText(){
        return this.text.getText();
    }

    public String getLink(){

        return this.link;
    }
    public void updateHyperLink(String textIn, String givenLink) {
        this.text.setText(textIn);
        this.link = givenLink;
    }
    public void end() {
        if (getMedia().getText().equals(this.text.getText())) {
            getMedia().setText(this.text.getText());
        }

    }

}
