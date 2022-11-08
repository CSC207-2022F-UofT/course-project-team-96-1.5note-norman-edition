package app.media;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


public class PenStroke extends Media {

    public static record Segment(
            double endX, double endY) {}

    private List<Segment> segments;
    private double thickness;

    public PenStroke(double x, double y, double thickness) {
        super("pen-stroke", x, y, 0, 0);
        segments = new ArrayList<>();
        this.thickness = thickness;
    }

    public void addSegment(Segment s) {
        segments.add(s);
    }

    public List<Segment> getSegments() {
        return Collections.unmodifiableList(segments);
    }
}
