package app.media;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Set;

public class MediaImage extends FileMedia{
    //A subclass of PageFileMedia, defining an Image that exists on the page
    //TODO: due to removal of image instance, this is currently no different from PageFileMedia

    public MediaImage(String name, double x, double y, double width, double height, byte[] rawData) {
        super(name, x, y, width, height, rawData);
    }
}