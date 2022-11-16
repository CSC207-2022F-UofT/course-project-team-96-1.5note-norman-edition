package app.media;

import java.util.ArrayList;

public class GenericShape extends Media {
    // TODO: Figure out if anything else needs to be done
    protected String colour;
    public GenericShape(double x, double y, double width, double height, String colour) {
        super("shape", x, y, width, height);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }


}
