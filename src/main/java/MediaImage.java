import javafx.scene.image.Image;

public class MediaImage extends PageFileMedia{
    //A subclass of PageFileMedia, defining an Image that exists on the page
    //Instance Attributes:
    //  -image: The GUI representation of the image in question
    private Image image;

    public MediaImage(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag,
                      byte[] rawData, Image image) {
        super(position, dimensions, angle, zIndex, id, tag, rawData);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
