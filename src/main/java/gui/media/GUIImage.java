package gui.media;

import app.media.MediaImage;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Draws image into the page. Interfaces with ImageTool and MediaImage to accomplish this.
 */
public class GUIImage extends GUIMedia<MediaImage> {

    private Image image;

    public void createInterface () throws Exception {
        byte[] buf = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        this.image = new Image();
    }
}
