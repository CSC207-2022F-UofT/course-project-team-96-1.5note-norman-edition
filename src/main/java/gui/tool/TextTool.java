package gui.tool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import gui.page.Page;
import gui.media.GUITextBox;

/**
 * Allows the user to create and edit textboxes. Click on an existing box to edit its content in real-time, or
 * type into the side panel box and drop the text down wherever you choose.
 *
 * Utilizes the GUITextBox class to visually display and edit the text, and TextBox to store the information itself.
 */
public class TextTool implements Tool {

    private HandlerMethod[] handlers;
    private Page page;

    private TextSettings settings;

    private GUITextBox currentText;

    public TextTool() {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::makeText)//,
                //new HandlerMethod<>(KeyEvent.KEY_RELEASED, this::updateEdit)
        };
        this.handlers = handlers;

        settings = new TextSettings();

        settings.getTextZone().textProperty().addListener(observable -> {
            if (currentText != null) {
                currentText.update(settings.getText());
                page.updateMedia(currentText);
            }
        });
    }

    @Override
    public String getName() { return "Text"; }

    @Override
    public HandlerMethod[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        finishEdit();
        this.page = null;
        settings.setText("");
    }

    @Override
    public FlowPane getSettingsGUI() { return settings; }

    private void makeText(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            EventTarget pick = e.getTarget(); // e.getTarget() returns type EventTarget, so I have to check what it is

            // Edit existing TextBox
            if (pick instanceof GUITextBox castpick) { // I have to use castpick here to make it work properly
                currentText = castpick;
                settings.setText(castpick.getText());
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

    private void finishEdit() {
        if (currentText != null) {
            currentText.end();
            page.updateMedia(currentText);
            currentText = null;
        }
    }


}

/**
 * Defines the "settings control panel" on the left so that the text boxes can be managed with ease. Used for setting
 * up the typeable area and controling the current target text the user is manipulating.
 */
class TextSettings extends FlowPane {

    private static int PADDING = 5;
    private TextArea textZone;

    public TextSettings() {
        textZone = new TextArea();

        VBox textSettings = new VBox(
                PADDING, new Label("Enter Text"), textZone);
        textSettings.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textZone, Priority.ALWAYS);

        textZone.setPrefWidth(280);

        getChildren().addAll(textSettings);
    }

    public String getText() { return textZone.getText(); }
    public TextArea getTextZone() { return this.textZone; }

    public void setText(String textIn) {
        this.textZone.setText(textIn);
    }
}
