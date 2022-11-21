package gui.tool;
import gui.page.Page;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventTarget;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import gui.media.GUIHyperlinkBox;
import javafx.scene.paint.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;

public class HyperlinkTool implements Tool {

    private Page page;
    private HandlerMethod[] handlers;
    private HyperlinkSettings settings;
    private GUIHyperlinkBox currentText;
    private ObjectProperty<Color> colour;

    public HyperlinkTool(ObjectProperty<Color> colour) {
        HandlerMethod[] handlers = {
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::interact)//,
                //new HandlerMethod<>(KeyEvent.KEY_PRESSED, this::updateEdit),
                //new HandlerMethod<>(MouseEvent.MOUSE_EXITED_TARGET, this::endEdit)

        };
        this.handlers = handlers;
        settings = new HyperlinkSettings();
        this.colour = colour;
    }
    public void interact(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            makeHyperlink(e);

        } else if (e.getButton() == MouseButton.SECONDARY) {
            e.consume();
            clickLink(e);
        }
    }

    public void clickLink (MouseEvent e) {
        EventTarget pick = e.getTarget();

        if (pick instanceof GUIHyperlinkBox) {
            try {
                GUIHyperlinkBox castpick = (GUIHyperlinkBox) pick; //casting to use the methods of GUIHyperlinkbox
                String clicked = castpick.getLink();

                URI url = new URI(clicked);
                Desktop.getDesktop().browse(url);
            }
            catch (Exception l){
                System.out.println("invalid link");
            }
        }
    }

    public void makeHyperlink(MouseEvent e){
       /* if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();*/

        EventTarget pick = e.getTarget();

        // editing text and hyperlink doin funny stuff; TODO fix
/*        if (pick instanceof GUIHyperlinkBox) {
            GUIHyperlinkBox castpick = (GUIHyperlinkBox) pick; //casting to use the methods of GUIHyperlinkbox

            settings.setText(castpick.getText()); // retrieve the text from the page and putting it in the tools box
            settings.setText(castpick.getLink()); //retrieve link from page
            //System.out.println(castpick.getText() + castpick.getLink());


            // castpick.updateHyperLink(settings.getText(), settings.getLink()); // update text on the page
            // why does this do weird things with the updating
            //this.currentText = castpick;
            //page.updateMedia();


        }*/
        // Creates new TextBox in empty space
        //else{
            currentText = new GUIHyperlinkBox(
                    page.getMouseCoords(e),
                    settings.getText(), settings.getLink(), colour.getValue()
            );

        // adds link to page if it is valid
        if (settings.checkLink(settings.getLink())) {
            page.addMedia(currentText);
            page.updateMedia(currentText);
/*            settings.setLink("");
            settings.setText("");*/
        }
        // alert message if link isn't valid
        else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid link inputted");
            a.setContentText("Please enter a valid link");
            a.show();
        }
    }

    private void finishEdit(){
        if (currentText != null){
            currentText.end();
            page.updateMedia(currentText);
        }
    }

    // getter methods
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
    }
    // link validator
    public boolean checkLink  (String givenLink){
        try{
            new URL(givenLink).openStream().close();
            return true;
        }
        catch (Exception notLink){
            return false;
        }
    }

    public String getText() { return textBox.getText(); }
    public void setText(String textIn) {this.textBox.setText(textIn);}
    public String getLink() {return linkBox.getText();}

    public void setLink(String givenLink) {this.linkBox.setText(givenLink);} // setText from TextArea class
    public TextArea getTextBox() { return this.textBox; }
}
