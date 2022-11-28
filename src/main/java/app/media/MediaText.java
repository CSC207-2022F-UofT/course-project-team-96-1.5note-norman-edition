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
    }

    public MediaText(double x, double y) {
        super("text-box", x, y, 0, 0);
        this.text = "";
    }

    /*
    public TextBox(double x, double y, String text, String link, String colour){
        super("text-box", x, y, 0, 0);
        this.text = text;
        this.link = link;
        this.colour = colour;
    }
    */

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    /*
    public void setLink(String link){
        this.link = link;

    }

    public String getLink(){
        return this.link;
    }
    */

    public String getColour(){
        return colour;
    }

}
