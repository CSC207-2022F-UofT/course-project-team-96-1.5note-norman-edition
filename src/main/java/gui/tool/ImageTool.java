package gui.tool;

import app.controllers.ToolBarController;
import gui.page.Page;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Tool for the toolbar to insert images onto the page. Interfaces with MediaImage, ImageModifier and GUIImage to do this.
 */
public class ImageTool implements Tool {

    private Page page;

    private final ImageSettings settings;

    private ImageView currentImage;

    /**
     * Default constructor, used to set up the tool and detect actions, then trigger image insertion.
     */
    public ImageTool() {
        this.settings = new ImageSettings();

        settings.getInsertImage().setOnAction(e -> {
            try {
                ToolBarController tbc = new ToolBarController();
                Bounds bounds = page.getVisibleBounds();
                tbc.insertImage(page.getCommunicator(), bounds.getCenterX(), bounds.getCenterY());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    @Override
    public Node getSettingsGUI() {
        return settings;
    }

    @Override
    public void enabledFor(Page page) {
        this.page = page;
    }

    @Override
    public String getName() {
        return "Image";
    }
}

/**
 * Defines settings panel on the left for tool usage. In this case, just the "import image" button.
 */
class ImageSettings extends VBox {

    private final Button insertImage;

    public ImageSettings() {
        this.insertImage = new Button("Insert Image");

        setSpacing(30);
        getChildren().addAll(insertImage);
    }

    public Button getInsertImage() {
        return this.insertImage;
    }
}