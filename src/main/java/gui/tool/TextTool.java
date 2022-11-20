package gui.tool;

import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.*;

import app.media.*;
import gui.media.*;
import gui.page.Page;
import gui.media.GUITextBox;


public class TextTool implements Tool {

    private HandlerMethod[] handlers;
    private Page page;

    private TextSettings settings;

    private GUITextBox currentText;

    public TextTool() {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::makeText),
                new HandlerMethod<>(KeyEvent.KEY_RELEASED, this::updateEdit)
        };
        this.handlers = handlers;

        settings = new TextSettings();
    }

    @Override
    public String getName() { return "Text"; }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public void enabledFor(Page page) { this.page = page; }

    @Override
    public void disabledFor(Page page) {
        finishEdit();
        this.page = null;
    }

    @Override
    public FlowPane getSettingsGUI() { return settings; }

    private void makeText(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            EventTarget pick = e.getTarget(); // e.getTarget() returns type EventTarget, so I have to check what it is

            // Edit existing TextBox
            if (pick instanceof GUITextBox castpick) { // I have to use castpick here to make it work properly
                settings.setText(castpick.getText());
                currentText = castpick;
            }
            // Create new TextBox in empty space
            else{
                currentText = new GUITextBox(
                        page.getMouseCoords(e),
                        settings.getText()
                );
                page.addMedia(currentText);
                page.updateMedia(currentText);
            }
        }
    }

    private void updateEdit(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            e.consume();

            currentText.update(settings.getText());
            page.updateMedia(currentText);
        }
    }

    private void finishEdit() {
        if (currentText != null) {
            currentText.end();
            page.updateMedia(currentText);
            currentText = null;
        }
    }


}

class TextSettings extends FlowPane {

    private static int PADDING = 5;
    private TextArea textBox;

    public TextSettings() {
        textBox = new TextArea();

        VBox textSettings = new VBox(
                PADDING, new Label("Enter Text"), textBox);
        textSettings.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        getChildren().addAll(textSettings);
    }

    public String getText() { return textBox.getText(); }
    public TextArea getTextBox() { return this.textBox; }

    public void setText(String textIn) { this.textBox.setText(textIn); }
}
