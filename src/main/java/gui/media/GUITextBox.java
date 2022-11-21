package gui.media;

import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.*;

import app.media.TextBox;
import app.media.Media;

public class GUITextBox extends GUIMedia<TextBox> {

    private Text text;

    private void setInitialValues() {
        this.text = new Text("");

        getChildren().clear();
        getChildren().addAll(this.text);
    }

    public GUITextBox(Point2D point, String text, Color colour) {

        super(new TextBox(point.getX(), point.getY(), text, colour.toString()));

        setInitialValues();
        setText(text);
        this.text.setFill(colour);
    }

    public GUITextBox(TextBox media) {
        super(media);
        mediaUpdated(media);
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

    private void setText(String textIn) { this.text.setText(textIn); }

    public void update(String textIn) {
        this.text.setText(textIn);
    }

    public void end() {
        if (getMedia().getText().equals(this.text.getText())) {
            getMedia().setText(this.text.getText());
        }

    }

}