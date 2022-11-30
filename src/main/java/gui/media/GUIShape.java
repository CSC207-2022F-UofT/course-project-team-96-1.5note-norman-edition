package gui.media;

import gui.tool.app.media.GenericShape;
import gui.tool.app.media.Media;
import javafx.geometry.Point2D;

/**
 * A sub-class of GUIMedia representing an arbitrary shape
 */
public abstract class GUIShape extends GUIMedia<GenericShape> {

    public GUIShape(GenericShape media) {
        super(media);
    }

    /**
     * Called when changing the position or dimensions of a shape
     * @param p1 The first point used to define the shape
     * @param p2 The second point used to define the shape
     * @param sameSideLengths Whether the side lengths should be made equal (The bounding box will be shortened so that this is possible)
     */
    public abstract void update(Point2D p1, Point2D p2, boolean sameSideLengths);

    /**
     * {@inheritDoc}
     */
    @Override
    public void mediaUpdated(Media media) {
        GenericShape newShape = (GenericShape)  media;
        GenericShape currentShape = getMedia();

        if (currentShape != newShape) {
            setMedia(newShape);
            setGenericShape(newShape);
        }
    }

    /**
     * Creates a GUIShape from its corresponding GenericShape
     * @param shape A GenericShape to be represented as a new GUIShape
     */
    public abstract void setGenericShape(GenericShape shape);

    /**
     * Returns the center and dimensions of a shape's bounding box, given two points
     * <p>
     * Used to define the area a shape should occupy, given two points
     * @param origin The origin of the bounding box (should be the "anchored" corner)
     * @param current The other corner of the bounding box (should be the "moveable" corner)
     * @param sameSideLengths Whether to return a bounding box with equal side lengths (square) as opposed to the default (rectangle)
     * @return An array of doubles containing the horizontal and vertical midpoints and the width and height of the bouding box
     */
    public double[] RestrictPoints (Point2D origin, Point2D current, boolean sameSideLengths) {
        double x1 = origin.getX();
        double y1 = origin.getY();
        double x2 = current.getX();
        double y2 = current.getY();

        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        if (sameSideLengths) {
            width = Math.min(width, height);
            height = width;
        }

        // Gives us the current point clamped within (width, height) of the origin
        x2 = clamp(x2, x1 - width, x1 + width);
        y2 = clamp(y2, y1 - height, y1 + height);

        return new double[]{(x1+x2)/2, (y1+y2)/2, width, height};
    }

    /**
     * A Helper method which clamps a value between a minimum and a maximum
     * @param value The value to be clamped
     * @param minimum The minimum boundary
     * @param maximum The maximum boundary
     * @return The clamped value
     */
    public double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }

    /**
     * Returns the string representation of the GUIShape's GenericShape class
     * @return The string representation of the GUIShape's GenericShape class
     */
    @Override
    public String toString() {
        return getMedia().toString();
    }
}

