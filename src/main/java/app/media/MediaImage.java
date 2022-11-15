package app.media;
import java.util.Set;

public class MediaImage extends FileMedia{
    //A subclass of PageFileMedia, defining an Image that exists on the page
    //TODO: due to removal of image instance, this is currently no different from PageFileMedia

    public MediaImage(long id, String name, Set<String> tags, double x, double y,
                      double width, double height, double angle, int zIndex, byte[] rawData) {
        super(id, name, tags, x, y, width, height, angle, zIndex, rawData);
    }
}
