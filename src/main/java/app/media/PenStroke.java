package app.media;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;


/**
 * Entity class for a pen stroke.
 *
 * A pen stroke is defined by a starting position, a thickness, a colour, and
 * a list of ending positions for the line segments which make up the stroke.
 */
public class PenStroke extends Media {

    /** Data for a single line segment in a stroke. */
    public static record Segment(double endX, double endY)
            implements Serializable {}

    // A pen stroke is defined by a list of line segments and the
    // colour + thickness for those line segments.
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
