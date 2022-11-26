package app.media;

/**
 * An implementation of GenericShape representing a rectangle
 */
public class RectangleShape extends GenericShape {
    /**
     * Initializes a rectangle with the following settings
     * @param x The shape's x position (Top left corner of bounding box)
     * @param y The shape's y position (Top left corner of bounding box)
     * @param width The shape's width
     * @param height The shape's height
     * @param colour The shape's color
     */
    public RectangleShape(double x, double y, double width, double height, String colour) {
        super("Rectangle", x, y, 0, 0, colour);
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
