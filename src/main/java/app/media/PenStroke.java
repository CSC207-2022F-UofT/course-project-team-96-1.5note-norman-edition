package app.media;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;


public class PenStroke extends Media {

    public static record Segment(double endX, double endY)
            implements Serializable {}

    private List<Segment> segments;
    private double thickness;
    private String colour;

    public PenStroke(double x, double y, double thickness, String colour) {
        super("pen-stroke", x, y, 0, 0);
        segments = new ArrayList<>();
        this.thickness = thickness;
        this.colour = colour;
    }

    public void addSegment(Segment s) {
        segments.add(s);
    }

    public List<Segment> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    public double getThickness() {
        return thickness;
    }

    public String getColour() {
        return colour;
    }
}
