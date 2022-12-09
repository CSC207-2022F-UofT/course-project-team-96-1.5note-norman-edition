package app.media;

import java.io.Serializable;

public class MediaText extends Media {

    public static record Box (double leftCorner, double rightCorner)
        implements Serializable {}

    // textbox defined by the text in the box, font colour
    private String text;
    private String colour;


    public MediaText(double x, double y, String text, String colour) {
        super("text-box", x, y, 0, 0);
        this.text = text;
        this.colour = colour;
        setZindex(-1);
    }

    public MediaText(double x, double y) {
        super("text-box", x, y, 0, 0);
        this.text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public String getColour(){
        return colour;
    }

}
