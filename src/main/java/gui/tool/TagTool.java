package gui.tool;

import app.controllers.ToolBarController;
import gui.media.GUIMedia;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.event.*;
import javafx.scene.input.MouseEvent;


public class TagTool implements Tool{
    private final TagSettings settings;
    private final HandlerMethod<?>[] handlers;
    private final Button tagButton = new Button("Tag");
    private GUIMedia<?> media;
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
    public HandlerMethod<?>[] getHandlerMethods() {
        return handlers;
    }

    @Override
    public FlowPane getSettingsGUI(){
        return settings;
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
            }
            else{
                tagButton.setDisable(true);
            }
        }
    }
    class TagSettings extends FlowPane{
        public TagSettings(){
            // Creates a textfield for the tag and tag button to assign the tag to media
            TextField createTag = new TextField();
            tagButton.setDisable(true);

            // The behaviour of the tag button depending on the user click
            tagButton.setOnMouseClicked(TagTool.this::getMedia);

            // Tagging the media
            tagButton.setOnAction(e->{
                ToolBarController tb = new ToolBarController();
                tb.tag(createTag, media);
                createTag.clear();

            });

            // Visually setting the textfield and button
            int PADDING = 5;
            HBox tagging = new HBox(PADDING, new Label("Tag Name"), createTag, tagButton);
            tagging.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(tagging, Priority.ALWAYS);

            //Adding it to the GUI
            getChildren().addAll(tagging);

        }
    }
}
