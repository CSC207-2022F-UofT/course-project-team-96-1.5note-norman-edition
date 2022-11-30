package gui.media;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.Point2D;

import gui.tool.app.media.PenStroke;
import gui.tool.app.media.Media;


/**
 * GUI representation of a pen stroke on a page.
 *
 * The pen stroke is made from consecutive line segments. A new segment is
 * added when the direction of the mouse movement changes significantly.
 */
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
        addSegment(new Segment(0, 0));

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

    // display the given PenStroke entity, adding all its segments all at once.
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

        // Distance between the cursor and the end of the last segment
        double dist = Math.sqrt(
            Math.pow((x - startX) - s.startX, 2)
            + Math.pow((y - startY) - s.startY, 2));

        // Direction of the last segment
        Point2D dir = s.getDirection();

        // Dot product between the direction of the current segment and the
        // direction of the previous segment
        double prevSegmentDot = dir.dotProduct(prevSegmentDir);

        // Dot product between the direction of the current segment and the
        // "average direction"
        double avgDot = dir.dotProduct(avgDir);

        // Mean average of prevSegmentDot and avgDot
        double dirDot = (prevSegmentDot + avgDot) / 2;

        /*
         * If dirDot / dist or avgDot fall below the given thresholds, end
         * the current segment at the current cursor position and start the
         * next segment.
         *
         * Additionally, a new segment will *not* be added if `straight` is set
         * to true.
         */
        if (!straight && (5 * dirDot / dist < 1 || avgDot < 0.2)) {
            prevSegmentDir = dir;
            avgDir = dir;
            commitSegment(s);
            addSegment(new Segment(x - startX, y - startY));
        }

        // Update avgDir to equal the mean average of its current value and
        // the value of `dir`.
        avgDir = dir.add(avgDir).multiply(0.5);
    }

    /**
     * "End" the stroke. This commits the last segment to the underlying Media
     * object, if it hasn't been committed already.
     */
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


// Individual segment in the line.
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
