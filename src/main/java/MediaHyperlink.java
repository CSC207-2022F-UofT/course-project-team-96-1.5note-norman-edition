import javafx.scene.control.Hyperlink;

public class MediaHyperlink extends PageMedia {
    //A subclass of PageMedia, defining a Hyperlink that exists on the page
    //Instance Attributes:
    //  -text: The GUI representation of the hyperlink in question
    private Hyperlink text;

    public MediaHyperlink(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag,
                     Hyperlink text) {
        super(position, dimensions, angle, zIndex, id, tag);
        this.text = text;
    }

    public Hyperlink getText() {
        return text;
    }

    public void setText(Hyperlink text) {
        this.text = text;
    }
}
