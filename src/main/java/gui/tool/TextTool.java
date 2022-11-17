package gui.tool;

import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.*;

import gui.page.Page;
import gui.media.GUITextBox;


public class TextTool implements Tool {

    private HandlerMethod[] handlers;
    private Page page;

    private TextSettings settings;

    private GUITextBox currentText;

    public TextTool() {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::makeText)//,
                //new HandlerMethod<>(KeyEvent.KEY_PRESSED, this::updateEdit),
                //new HandlerMethod<>(MouseEvent.MOUSE_EXITED_TARGET, this::endEdit)
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

            Node pick = e.getPickResult().getIntersectedNode();
            //System.out.println(pick.getClass());

            // Edit existing TextBox
            if (pick instanceof Text) { //TODO: Incorrectly returns Text, change to GUITextBox once Dexter fixes it!
                ((GUITextBox) pick).update(settings.getText());
            }
            // Create new TextBox in empty space
            else{
                currentText = new GUITextBox(
                        page.getMouseCoords(e),
                        settings.getText()
                );
                page.addMedia(currentText);
            }
        }
    }

    /*private void updateEdit(KeyEvent e) {
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
    }*/

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