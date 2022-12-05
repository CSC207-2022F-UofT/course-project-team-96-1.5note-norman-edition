package gui.tool;

import app.controllers.ToolBarController;
import gui.media.GUIMedia;
import gui.page.Page;
import gui.ResourceLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.event.*;
import javafx.scene.input.MouseEvent;

import java.util.Objects;


public class TagTool implements Tool{
    private final TagSettings settings;
    private final HandlerMethod<?>[] handlers;
    private final Button tagButton = new Button("Tag");
    private final Button removeTagButton = new Button("Remove Tag");
    private GUIMedia<?> media;
    private Page page;
    private final String tagStatement = "This Object's Tags: ";
    private final Label action = new Label(tagStatement);
    public TagTool(){
        settings = new TagSettings();
        // Handler methods for when the user clicks on something on the page
        this.handlers = new HandlerMethod<?>[]{new HandlerMethod<>(MouseEvent.MOUSE_CLICKED, this::getMedia)};
    }

    @Override
    public String getName(){
        return "Tag";
    }

    @Override
    public Node getGraphic() {
        return new Label(
                getName(), ResourceLoader.loadSVGicon("icons/tag.svg", 15, 15));
    }

    @Override
    public HandlerMethod<?>[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public FlowPane getSettingsGUI(){
        return settings;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public void disabledFor(Page page) {
        this.page = null;
    }

    // Method to get the media that the user clicks on the page
    // Enables/Disables the Tag button depending on what they click on
    public void getMedia(MouseEvent e){
        if (e.getButton() == MouseButton.PRIMARY){
            e.consume();
            EventTarget clicked = e.getTarget();
            if (clicked instanceof GUIMedia) {
                media = (GUIMedia<?>) clicked;
                tagButton.setDisable(false);
                removeTagButton.setDisable(media.getMedia().getTags().isEmpty());
                updateStatement();
            }
            else{
                tagButton.setDisable(true);
                removeTagButton.setDisable(true);
                clearStatement();
            }
        }
    }

    public void updateStatement(){
        StringBuilder results = new StringBuilder();
        for (String tag: media.getMedia().getTags()){
            if(!tag.isEmpty()) {
                results.append("\n").append(tag);
            }
        }
        action.setText(tagStatement + results);
    }

    public void clearStatement(){
        action.setText(tagStatement);
    }

    class TagSettings extends FlowPane{
        public TagSettings(){

            // Creates a textfield for the tag and tag button to assign the tag to media
            TextField tagName = new TextField();
            tagName.setPrefWidth(100);
            tagButton.setDisable(true);
            // The behaviour of the tag button depending on the user click
            tagButton.setOnMouseClicked(TagTool.this::getMedia);
            // Tagging the media
            tagButton.setOnAction(e->{
                ToolBarController tb = new ToolBarController();
                tb.tag(tagName.getText(), media);
                page.updateMedia(media);
                updateStatement();

            });

            //Makes sure that the button is not enabled when there is nothing to remove
            removeTagButton.setDisable(true);
            // behaviour of the button matching that of the tagging button and other cases
            removeTagButton.setOnMouseClicked(TagTool.this::getMedia);
            // removing the tag of the media
            removeTagButton.setOnAction(e->{
                ToolBarController toolBarController = new ToolBarController();
                toolBarController.removeTag(tagName.getText(), media);
                page.updateMedia(media);
                updateStatement();
            });




            // Visually setting the textfield and button
            int PADDING = 5;
            HBox tagging = new HBox(PADDING, new Label("Tag"), tagName, tagButton, removeTagButton);
            tagging.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(tagging, Priority.ALWAYS);

            //Adding it to the GUI
            getChildren().addAll(tagging, action);

        }
    }
}
