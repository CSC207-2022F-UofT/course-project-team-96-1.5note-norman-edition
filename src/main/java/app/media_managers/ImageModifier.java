package app.media_managers;

import app.MediaCommunicator;
import app.media.Media;
import app.media.MediaImage;
import storage.FileLoaderWriter;
import storage.Storage;

import java.util.HashMap;

public class ImageModifier{
    /**
     * Manages creation/interactions on MediaImage
     * <p>
     * Class stores instances of a MediaImage to modify and add.
     */
    private String caption;

    private static MediaImage image;

    public static void addMedia(MediaCommunicator com) throws Exception {
        // Load image based on user selection
        Storage fileManager = new FileLoaderWriter();
        HashMap<String, byte[]> fileData = fileManager.readFile(new String[]{"*.png", "*.jpg", "*.jpeg", ".gif", "*.JPG"},
                "Image (.png, .jpg, .jpeg, .gif, .JPG)");
        if (fileData != null) {
            String fileName = (String) (fileData.keySet().toArray())[0];
            image = new MediaImage(fileName, 200, 200, 200, 200,
                    fileData.get(fileName));
            com.updateMedia(image);
        }
    }

    public void setCaption(String givenCaption) {
        this.caption = givenCaption;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setImage(MediaImage image) {
        this.image = image;
    }

    public MediaImage getImage() {
        return this.image;
    }
}
