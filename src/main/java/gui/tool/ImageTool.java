package gui.tool;

import app.controllers.ToolBarController;
import app.media_managers.ImageModifier;
import gui.page.Page;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ImageTool implements Tool {

    private Page page;

    private final ImageSettings settings;

    private ImageView currentImage;

    public ImageTool() {
        this.settings = new ImageSettings();

        settings.getInsertImage().setOnAction(e -> {
            try {
                ImageModifier.addMedia();
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