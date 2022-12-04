package gui.tool;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;


/**
 * Visual respresentation of box selection.
 * <p>
 * The rectangle doesn't block mouse input, so it can be displayed above the
 * page without interfering with mouse events.
 * <p>
 * The rectangle is sized with its `update` method which takes 2 points.
 */
public class BoxSelectRectangle extends Rectangle {

    private static final Color COLOUR = Color.DODGERBLUE;

    public BoxSelectRectangle() {
        super();
        setMouseTransparent(true);
        setFill(COLOUR.deriveColor(0, 1, 1, 0.5));
        setStroke(COLOUR);
    }

    /**
     * Reposition/resize the rectangle such that 2 opposite corners lie on the
     * points given as `p1` and `p2`.
     */
    public void update(Point2D p1, Point2D p2) {
        double width = Math.abs(p1.getX() - p2.getX());
        double height = Math.abs(p1.getY() - p2.getY());

        double minX = Math.min(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());

        setX(minX);
        setY(minY);
        setWidth(width);
        setHeight(height);
    }
}
