package app.media;
import java.util.Set;

public class MediaText extends Media {
    //A subclass of PageMedia, defining a text that exists on the page
    //Instance Attributes:
    //  -text: Text held by this class
    private String text;

    public MediaText(long id, String name, Set<String> tags, double x, double y,
                     double width, double height, double angle, int zIndex, String text) {
        super(id, name, tags, x, y , width, height, angle, zIndex);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
