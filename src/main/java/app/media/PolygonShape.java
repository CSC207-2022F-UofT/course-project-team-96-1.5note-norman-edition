package app.media;

import javafx.geometry.Point2D;

/**
 * An implementation of GenericShape representing a polygon
 * <p>
 * We assume the polygon is a regular shape, i.e. that each interior angle and each side length are the same.
 */
public class PolygonShape extends GenericShape {
    /**
     * Represents the number of sides of the polygon
     */
    private int sideCount;

    /**
     * Represents the starting angular offset of the polygon after creation
     */
    private double startAngle;

    /**
     * Represents the distance from the center of the polygon to any vertex
     */
    private double radius;

    /**
     * Initializes a polygon with the following settings
     * @param p1 The position of the polygon's center
     * @param p2 The position which determines the polygon's radius and starting angle
     * @param colour The shape's color
     * @param radius The shape's radius
     * @param startAngle The shape's starting angle
     * @param sideCount The number of sides the shape has
     */
    public PolygonShape(Point2D p1, Point2D p2, String colour, double radius, double startAngle, int sideCount) {
        super("Polygon", p1, p2, colour);
        this.radius = radius;
        this.startAngle = startAngle;
        this.sideCount = sideCount;
    }

    /**
     * Returns the radius of the polygon
     * <p>
     * Since the polygons are all regular shapes, this represents the distance from its center towards any vertex
     * @return The radius of the polygon
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Returns the starting angle of the polygon
     * <p>
     * More specifically, this is the angle the polygon is rotationally offset by
     * @return The rotational offset of the polygon
     */
    public double getStartAngle() {
        return startAngle;
    }

    /**
     * Returns the number of sides of the polygon
     * @return the number of sides of the polygon
     */
    public int getSideCount() {
        return sideCount;
    }

    /**
     * Sets the radius of the polygon
     * @param radius The desired new radius of the polygon
     */

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Sets the starting angle of the polygon
     * <p>
     * More specifically, this is the angle the Polygon is to be rotationally offset by
     * @param startAngle The desired new rotational offset of the Polygon
     */

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    /**
     * Sets the side count of the polygon
     * @param sideCount The new number of sides the polygon should have
     */
    public void setSideCount(int sideCount) {
        this.sideCount = sideCount;
    }

    /**
     * Returns the shape's type, position, dimensions and color
     * <p>
     * Additionally, returns the starting angular offset and side count (unique to polygon)
     * @return the shape's type, position, dimensions and color
     */
    @Override
    public String toString() {
        return "Polygon: x["+super.getX()+"] y["+super.getY()+"] r["+this.getRadius()+"] a["+this.getStartAngle()+"] s["+this.getSideCount()+"] c["+super.getColour()+"]";
    }
}
