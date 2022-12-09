package app.media;

import javafx.geometry.Point2D;

/**
 * An implementation of GenericShape representing an ellipse
 */
public class EllipseShape extends GenericShape {
    /**
     * Initializes an ellipse with the following settings
     * @param p1 The first point defining the shape (often the origin point)
     * @param p2 The second point defining the shape (often the point bound)
     * @param colour The shape's color
     */
    public EllipseShape(Point2D p1, Point2D p2, String colour) {
        super("Ellipse", p1, p2, colour);
        getTags().add("ellipse");
    }

    /**
     * Returns the shape's type, position, dimensions and color
     * @return the shape's type, position, dimensions and color
     */
    @Override
    public String toString() {
        return "Ellipse: x["+super.getX()+"] y["+super.getY()+"] w["+super.getWidth()+"] h["+super.getHeight()+"] c["+super.getColour()+"]";
    }
}
