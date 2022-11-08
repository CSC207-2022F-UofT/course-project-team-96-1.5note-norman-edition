import javafx.scene.image.Image;

public class MediaImage extends PageMedia{
    private Image image; // TODO: Temp object type, feel free to change as needed.

    public MediaImage(double[] position, double[] dimensions, double angle, int zIndex, String name, String tag,
                      byte[] rawData, Image image) {
        super(position, dimensions, angle, zIndex, name, tag, rawData);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
