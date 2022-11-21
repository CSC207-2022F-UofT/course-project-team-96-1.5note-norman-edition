package gui.media;

import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import app.media.TextBox;
import app.media.Media;

/**
 * Visually draws the TextBoxes in the canvas area. Interfaces with TextTool and TextBox to accomplish this.
 */
public class GUITextBox extends GUIMedia<TextBox> {

    private Text text;

    private void setInitialValues() {
        this.text = new Text("");

        getChildren().clear();
        getChildren().addAll(this.text);
    }

    public GUITextBox(Point2D point, String text) {
        super(new TextBox(point.getX(), point.getY(), text));

        setInitialValues();
        setText(text);
    }

    public GUITextBox(TextBox media) {
        super(media);
        mediaUpdated(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        TextBox newText = (TextBox) media;

        setInitialValues();
        setMedia(newText);
        setText(newText.getText());
        System.out.println(newText.getText());
    }

    private void setText(String textIn) {
        this.text.setText(textIn);
        this.getMedia().setText(textIn);
    }

    public String getText() { return this.text.getText(); }

    public void update(String textIn) {
        this.text.setText(textIn);
        this.getMedia().setText(textIn);
    }

    public void end() {
        if (getMedia().getText().equals(this.text.getText())) {
            getMedia().setText(this.text.getText());
        }
    }
}
