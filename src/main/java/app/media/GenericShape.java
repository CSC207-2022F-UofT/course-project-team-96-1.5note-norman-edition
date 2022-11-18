package app.media;

import java.util.ArrayList;

public abstract class GenericShape extends Media {
    protected String colour;
    public GenericShape(String type, double x, double y, double width, double height, String colour) {
        super(type, x, y, 0, 0);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }


}

