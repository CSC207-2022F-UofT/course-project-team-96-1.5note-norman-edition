package app.media_managers;

import app.MediaCommunicator;
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

    /**
     * Loads images into the page from a file, passing back to MediaCommunicator to be displayed.
     *
     * @param com MediaCommunicator of the page
     * @param x X coordinate of top-left corner of image once placed
     * @param y Y coordinate of top-left corner of image once played
     * @throws Exception Throws exception upon failing to find proper file.
     */
    public static void addMedia(MediaCommunicator com, double x, double y) throws Exception {
        // Load image based on user selection
        Storage fileManager = new FileLoaderWriter();
        HashMap<String, byte[]> fileData = fileManager.readFile(new String[]{"*.png", "*.jpg", "*.jpeg", ".gif", "*.JPG"},
                "Select Image Type");
        if (fileData != null) {
            String fileName = (String) (fileData.keySet().toArray())[0];

            image = new MediaImage(fileName, x - 100, y - 100, 200, 200,
                    fileData.get(fileName));
            com.updateMedia(image);
        }
    }

    //These methods below here are never used, but left in for possible future extension.

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
