package gui.tool;

import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.beans.property.*;

import gui.page.PageEventHandler.*;
import gui.page.Page;
import gui.media.GUITextBox;


public class TextTool implements Tool {

    private HandlerMethod[] handlers;
    private Page page;

    private textSettings settings;

    private GUITextBox currentText;

    public TextTool() {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::startEdit),
                new HandlerMethod<>(KeyEvent.KEY_PRESSED, this::updateEdit),
                new HandlerMethod<>(MouseEvent.MOUSE_EXITED_TARGET, this::endEdit)
        };
        this.handlers = handlers;

        settings = new textSettings();
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

    private void startEdit(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            currentText = new GUITextBox(
                page.getMouseCoords(e),
                    settings.getText()
            );
            page.addMedia(currentText);
        }
    }

    private void updateEdit(KeyEvent e) {
        if (e.isShiftDown() && e.getCode() == KeyCode.ENTER) {
            e.consume();
            currentText.update(settings.getText());
        }
    }

    private void endEdit(MouseEvent e) {
        if (e.getTarget() != settings.getTextBox()) {
            e.consume();

            finishEdit();
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

class textSettings extends FlowPane {

    private static int PADDING = 5;
    private TextArea textBox;

    public textSettings() {
        textBox = new TextArea();

        HBox textSettings = new HBox(
                PADDING, new Label("Enter Text"), textBox);
        textSettings.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        getChildren().addAll(textSettings);
    }

    public String getText() { return textBox.getText(); }
    public TextArea getTextBox() { return this.textBox; }
}
