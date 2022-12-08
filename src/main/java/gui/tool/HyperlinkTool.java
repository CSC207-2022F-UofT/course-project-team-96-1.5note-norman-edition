package gui.tool;
import gui.page.Page;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import gui.media.GUIHyperlinkBox;
import gui.error_window.ErrorWindow;
import gui.ResourceLoader;
import javafx.scene.paint.*;
import java.awt.Desktop;
import javafx.geometry.Point2D;
import java.net.URI;
import java.net.URL;

public class HyperlinkTool implements Tool {

    private Page page;
    private final HandlerMethod<?>[] handlers;
    private final HyperlinkSettings settings;
    private GUIHyperlinkBox currentText;
    private final ObjectProperty<Color> colour;

    public HyperlinkTool(ObjectProperty<Color> colour) {
        this.handlers = new HandlerMethod[]{
                new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::interact)
        };
        settings = new HyperlinkSettings();
        this.colour = colour;
    }
    public void interact(MouseEvent e) {

        if (e.getButton() == MouseButton.PRIMARY) {
            e.consume();
            Point2D point = page.getMouseCoords(e);
            makeHyperlink(point, settings.getText(), settings.getLink(), colour.getValue());

        } else if (e.getButton() == MouseButton.SECONDARY) {
            e.consume();
            EventTarget pick = e.getTarget();
            clickLink(pick);
        }
    }

    public void clickLink (EventTarget pick) {

        if (pick instanceof GUIHyperlinkBox castpick) {
            //casting to use the methods of GUIHyperlinkbox
            String clicked = castpick.getLink();
            System.out.print(clicked + "link");

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

    public boolean makeHyperlink(Point2D point, String words, String link, Color colour) {

        // Creates new TextBox in empty space
        currentText = new GUIHyperlinkBox(point,
                words, link, colour);

        // adds link to page if it is valid
        if (checkLink(link) && currentText != null) {
            currentText.updateHyperLink(words, link);
            page.addMedia(currentText);
            page.updateMedia(currentText);
            return true;
        }
        // alert message if link isn't valid
        else {
            new ErrorWindow(
                    page, "Couldn't add Hyperlink",
                    "Please input a valid URL into the \"Enter Hyperlink\" text box.",
                    null, null).show();

        }
        return false;
    }

    // link validator
    // moved method outside settings class, so I could test this method and settings class is private
    public static boolean checkLink  (String givenLink){
        try{
            new URL(givenLink).openStream().close();
            return true;
        }
        catch (Exception notLink){
            return false;
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

    public GUIHyperlinkBox getCurrentHyperlinkBox (){
        return currentText;
    }

    @Override
    public HandlerMethod<?>[] getHandlerMethods() {
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
    private static final int PADDING = 5;
    private final TextArea textBox;
    private final TextArea linkBox;
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
        getChildren().addAll(textSettings, linkSettings);
    }

    public String getText() { return textBox.getText(); }
    public void setText(String textIn) {this.textBox.setText(textIn);}
    public String getLink() {return linkBox.getText();}
    public void setLink(String givenLink) {this.linkBox.setText(givenLink);} // setText from TextArea class
    // public TextArea getTextBox() { return this.textBox; }
}
