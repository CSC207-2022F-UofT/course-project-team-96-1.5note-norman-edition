import javafx.scene.text.*;

public class MediaText extends PageMedia {
    //A subclass of PageMedia, defining a text that exists on the page
    //Instance Attributes:
    //  -text: The GUI representation of the text in question
    private Text text;

    public MediaText(double[] position, double[] dimensions, double angle, int zIndex, long id, String tag,
                     Text text) {
        super(position, dimensions, angle, zIndex, id, tag);
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}