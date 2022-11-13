package gui.media;

import javafx.geometry.Point2D;

import app.media.TextBox;
import app.media.Media;

public class GUITextBox extends GUIMedia<TextBox> {

    private String text;

    private void setInitialValues() { this.text = ""; }

    public GUITextBox(Point2D point, String text) {
        super(new TextBox(point.getX(), point.getY(), text));

        setInitialValues();

    }

    public GUITextBox(TextBox media) {
        super(media);
        setText(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        TextBox newText = (TextBox) media;
        TextBox currentText = getMedia();

        if (currentText != newText) {
            setMedia(newText);
            setText(newText);
        }
    }

    private void setText(TextBox text) {
        setInitialValues();

        this.text = text.getText();
    }

    public void update(String text) {
        this.text = text;
    }

    public void end() {
        if (getMedia().getText().equals(this.text)) {
            getMedia().setText(this.text);
        }
    }
}
