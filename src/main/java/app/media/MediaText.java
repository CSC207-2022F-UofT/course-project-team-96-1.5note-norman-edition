package app.media;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Set;

public class MediaText extends Media {
    //A subclass of PageMedia, defining a text that exists on the page
    //Instance Attributes:
    //  -text: Text held by this class
    private String text;

    public MediaText(String name, double x, double y, double width, double height, String text) {
        super(name, x, y, width, height);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}