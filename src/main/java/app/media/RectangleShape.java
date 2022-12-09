package app.media;

import javafx.geometry.Point2D;

/**
 * An implementation of GenericShape representing a rectangle
 */
public class RectangleShape extends GenericShape {
    /**
     * Initializes a rectangle with the following settings
     * @param p1 First corner of the rectangle
     * @param p2 Opposite corner of the rectangle
     * @param colour The shape's color
     */
    public RectangleShape(Point2D p1, Point2D p2, String colour) {
        super("Rectangle", p1, p2, colour);
        getTags().add("rectangle");
    }

    /**
     * Returns the shape's type, position, dimensions and color
     * @return the shape's type, position, dimensions and color
     */
    @Override
    public String toString() {
        return "Rectangle: x["+super.getX()+"] y["+super.getY()+"] w["+super.getWidth()+"] h["+super.getHeight()+"] c["+super.getColour()+"]";
    }
}
