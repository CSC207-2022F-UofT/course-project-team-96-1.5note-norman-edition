package gui.media;

import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;

import app.media.TextBox;
import app.media.Media;

/**
 * Visually draws the TextBoxes in the canvas area. Interfaces with TextTool and TextBox to accomplish this.
 */
public class GUITextBox extends GUIMedia<TextBox> {

    private Text text;
    private Color colour;

    protected void setInitialValues() {
        this.text = new Text("");
        colour = Color.BLACK;

        getChildren().clear();
        getChildren().addAll(this.text);
    }

    public GUITextBox(Point2D point, String text, Color colour) {
        super(new TextBox(point.getX(), point.getY(), text, colour.toString()));

        setInitialValues();
        this.colour = colour;
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
        colour = Color.valueOf(newText.getColour());
        setText(newText.getText());
    }

    protected void setText(String textIn) {
        this.text.setFill(colour);
        this.text.setText(textIn);
        this.getMedia().setText(textIn);
    }

    protected Text getTextNode() {
        return text;
    }

    public String getText() { return this.text.getText(); }

    public void update(String textIn) {
        this.text.setText(textIn);
        this.getMedia().setText(textIn);
    }

    /**
     * Keeps the Text and Textbox in sync, and caps of calls during ending procedures when the tool is being
     * "switched off"
     */
    public void end() {
        if (getMedia().getText().equals(this.text.getText())) {
            getMedia().setText(this.text.getText());
        }
    }
}