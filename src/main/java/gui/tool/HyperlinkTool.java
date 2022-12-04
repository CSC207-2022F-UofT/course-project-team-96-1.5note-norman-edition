package gui.tool;
import gui.page.Page;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import gui.media.GUIHyperlinkBox;
import gui.error_window.ErrorWindow;
import gui.ResourceLoader;
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
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::interact)
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
            GUIHyperlinkBox castpick = (GUIHyperlinkBox) pick; //casting to use the methods of GUIHyperlinkbox
            String clicked = castpick.getLink();

            try {
                URI url = new URI(clicked);

                // https://bugs.openjdk.org/browse/JDK-8267572
                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        Desktop.getDesktop().browse(url);
                    } catch (Exception b) {
                        throw new RuntimeException(b);
                    }
                });
            } catch (Exception l) {
                System.out.println("invalid URL");
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
            new ErrorWindow(
                    page, "Couldn't add Hyperlink",
                    "Please input a valid URL into the \"Enter Hyperlink\" text box.",
                    null, null).show();
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
    public String getName() {return "Hyperlink";}

    @Override
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/hyperlink.svg", 15, 15));
    }

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
        super(PADDING, PADDING);

        textBox = new TextArea();
        linkBox = new TextArea();

        VBox textSettings = new VBox(
            PADDING, new Label("Enter text"), textBox);
        VBox linkSettings = new VBox(
                new Label ("Enter Hyperlink"), linkBox);

        // hyperlinkSettings.setAlignment(Pos.CENTER_LEFT);
        textBox.setPrefWidth(270);
        linkBox.setPrefWidth(270);

        // getChildren().addAll(hyperlinkSettings);
        getChildren().addAll(textSettings, linkSettings);
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
