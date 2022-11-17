package gui.tool;

import gui.page.Page;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.text.*;

import gui.media.GUIHyperlinkBox;


public class HyperlinkTool implements Tool {

    private Page page;
    private HandlerMethod[] handlers;
    private HyperlinkSettings settings;

    private GUIHyperlinkBox currentText;

    public HyperlinkTool() {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::makeHyperlink)//,
                //new HandlerMethod<>(KeyEvent.KEY_PRESSED, this::updateEdit),
                //new HandlerMethod<>(MouseEvent.MOUSE_EXITED_TARGET, this::endEdit)

        };
        this.handlers = handlers;
        settings = new HyperlinkSettings();
    }

    @Override
    public String getName() {return "hyperlink";}

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

    public void makeHyperlink(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();

            EventTarget pick = e.getTarget();

            /*if (pick instanceof GUIHyperlinkBox) {
                settings.setText(((GUIHyperlinkBox) pick).getText());
                ((GUIHyperlinkBox) pick).update(settings.getText());
            }
            // Create new TextBox in empty space
            */
                currentText = new GUIHyperlinkBox(
                        page.getMouseCoords(e),
                        settings.getText(), settings.getLink()
                );

                //currentText.setColour("blue");
                page.addMedia(currentText);



        }

    }
    private void finishEdit(){
        if (currentText != null){
            currentText.end();
            page.updateMedia(currentText);
            currentText = null;
        }
    }

}

class HyperlinkSettings extends FlowPane{
    private static int PADDING = 5;
    private TextArea textBox;
    private TextArea linkBox;
    public HyperlinkSettings () {
        textBox = new TextArea();
        linkBox = new TextArea();

        VBox hyperlinkSettings = new VBox(
            PADDING, new Label("Enter text"), textBox, new Label ("Enter Hyperlink"), linkBox);
        hyperlinkSettings.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);
        HBox.setHgrow(linkBox, Priority.ALWAYS);


        getChildren().addAll(hyperlinkSettings);
        System.out.println(getLink());

    }

    // getText and getTextBox methods fine
    public String getText() { return textBox.getText(); }
    public String getLink() {return linkBox.getText();}
    public TextArea getTextBox() { return this.textBox; }
}
