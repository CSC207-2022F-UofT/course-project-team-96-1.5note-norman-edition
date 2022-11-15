package app.media;

import javafx.scene.control.Hyperlink;

import java.util.Set;

public class MediaHyperlink extends Media{
    //A subclass of PageMedia, defining a Hyperlink that exists on the page
    //Instance Attributes:
    //  text: The GUI representation of the hyperlink in question
    //  link: Either a file path or a timestamp to be linked to
    private String text;
    private String link;

    public MediaHyperlink(long id, String name, Set<String> tags, double x, double y,
                          double width, double height, double angle, int zIndex, String text, String link) {
        super(id, name, tags, x, y, width, height, angle, zIndex);
        this.text = text;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
