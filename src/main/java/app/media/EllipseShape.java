package app.media;

/**
 * An implementation of GenericShape representing an ellipse
 */
public class EllipseShape extends GenericShape {
    /**
     * Initializes an ellipse with the following settings
     * @param x The shape's x position (Top left corner of bounding box)
     * @param y The shape's y position (Top left corner of bounding box)
     * @param width The shape's width
     * @param height The shape's height
     * @param colour The shape's color
     */
    public EllipseShape(double x, double y, double width, double height, String colour) {
        super("Ellipse", x, y, 0, 0, colour);
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
