package gui.media;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.Point2D;

import app.media.PenStroke;
import app.media.Media;


public class GUIPenStroke extends GUIMedia<PenStroke> {

    private List<Segment> segments;
    private double startX;
    private double startY;

    private Path path;
    private Circle startCircle;

    private Point2D prevSegmentDir;
    private Point2D avgDir;

    private void setInitialValues() {
        path = new Path(new MoveTo(0, 0));
        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.setStrokeLineJoin(StrokeLineJoin.ROUND);

        segments = new ArrayList<>();

        startCircle = new Circle();

        getChildren().clear();
        getChildren().addAll(path, startCircle);

        prevSegmentDir = Point2D.ZERO;
        avgDir = Point2D.ZERO;
    }

    public GUIPenStroke(Point2D point, double thickness, Color colour) {
        super(new PenStroke(point.getX(), point.getY(), thickness, colour.toString()));

        setInitialValues();
        segments.add(new Segment(0, 0));

        path.setStroke(colour);
        startCircle.setFill(colour);

        double x = point.getX();
        double y = point.getY();

        startX = x;
        startY = y;

        startCircle.setRadius(thickness / 2);
    }

    public GUIPenStroke(PenStroke media) {
        super(media);
        setStroke(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        PenStroke newStroke = (PenStroke) media;
        PenStroke currentStroke = getMedia();

        if (currentStroke != newStroke) {
            setMedia(newStroke);
            setStroke(newStroke);
        }
    }

    private void setStroke(PenStroke stroke) {
        setInitialValues();

        double thickness = stroke.getThickness();
        Color colour = Color.valueOf(stroke.getColour());

        startCircle.setRadius(thickness / 2);
        startCircle.setFill(colour);

        path.setStrokeWidth(thickness);
        path.setStroke(colour);

        for (PenStroke.Segment s: stroke.getSegments()) {
            addSegment(new Segment(s.endX(), s.endY()));
        }
    }

    public void update(Point2D point, double thickness, boolean straight) {
        double x = point.getX();
        double y = point.getY();

        startCircle.setRadius(thickness / 2);

        Segment s = getLastSegment();

        s.update(x - startX, y - startY);
        path.setStrokeWidth(thickness);

        double dist = Math.sqrt(
            Math.pow((x - startX) - s.startX, 2)
            + Math.pow((y - startY) - s.startY, 2));

        Point2D dir = s.getDirection();
        double prevSegmentDot = dir.dotProduct(prevSegmentDir);
        double avgDot = dir.dotProduct(avgDir);
        double dirDot = (prevSegmentDot + avgDot) / 2;

        if (!straight && 5 * dirDot / dist < 1 || avgDot < 0.2) {
            prevSegmentDir = dir;
            avgDir = dir;
            commitSegment(s);
            addSegment(new Segment(x - startX, y - startY));
        }

        avgDir = dir.add(avgDir).multiply(0.5);
    }

    public void end() {
        if (getMedia().getSegments().size() < segments.size()) {
            commitSegment(getLastSegment());
        }
    }

    private Segment getLastSegment() {
        return segments.get(segments.size() - 1);
    }

    private void addSegment(Segment s) {
        segments.add(s);
        path.getElements().add(s);
    }

    // add the segment into the underlying Media object.
    private void commitSegment(Segment s) {
            getMedia().addSegment(
                    new PenStroke.Segment(s.endX, s.endY));
    }
}

class Segment extends LineTo {

    double startX;
    double startY;

    double endX;
    double endY;

    public Segment(double x, double y) {
        super(x, y);

        startX = x;
        startY = y;

        endX = x;
        endY = y;
    }

    public void update(double x, double y) {
        endX = x;
        endY = y;

        setX(x);
        setY(y);
    }

    public Point2D getDirection() {
        double length = getLength();

        if (length == 0) {
            return Point2D.ZERO;
        } else {
            return new Point2D(endX - startX, endY - startY).multiply(1 / length);
        }
    }

    public double getLength() {
        return Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
    }
}
