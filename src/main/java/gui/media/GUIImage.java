package gui.media;

import app.media.Media;
import app.media.MediaImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

/**
 * Draws image into the page. Interfaces with ImageTool, ImageModifier and MediaImage to accomplish this.
 */
public class GUIImage extends GUIMedia<MediaImage> {

    private ImageView imageView;

    protected void setInitialValues() {
        setImageView(getMedia());

        getChildren().clear();
        getChildren().add(this.imageView);
    }
    public GUIImage (MediaImage image) {
        super(image);
        mediaUpdated(image);
        setInitialValues();
    }

    @Override
    public void mediaUpdated(Media media) {
        MediaImage newImage = (MediaImage) media;

        setInitialValues();
        setMedia(newImage);
        setImageView(newImage);
    }

    public void setImageView(MediaImage image) {
        ByteArrayInputStream bais = new ByteArrayInputStream(image.getRawData());
        this.imageView = new ImageView(new Image(bais));
    }
}
