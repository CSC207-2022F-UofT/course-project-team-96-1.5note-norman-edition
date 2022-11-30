package app.media;

/** Entity class for storing image media
 */
public class MediaImage extends FileMedia{
    public MediaImage(String name, double x, double y, double width, double height, byte[] rawData) {
        super(name, x, y, width, height, rawData);
        this.setRawData(rawData);
    }
}
