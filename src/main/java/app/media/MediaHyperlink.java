package app.media;

import javafx.scene.control.Hyperlink;

import java.util.Set;

public class MediaHyperlink extends Media{
    //A subclass of PageMedia, defining a Hyperlink that exists on the page
    //Instance Attributes:
    //  text: The GUI representation of the hyperlink in question
    private String text;

    public MediaHyperlink(long id, String name, Set<String> tags, double x, double y,
                          double width, double height, double angle, int zIndex, String text) {
        super(id, name, tags, x, y, width, height, angle, zIndex);
        this.text = text;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
