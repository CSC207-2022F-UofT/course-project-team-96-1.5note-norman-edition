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


public class TagTool implements Tool{
    private final TagSettings settings;
    private final HandlerMethod<?>[] handlers;
    private final Button tagButton = new Button("Tag");
    private GUIMedia<?> media;
    private Page page;

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
                page.updateMedia(media);
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
