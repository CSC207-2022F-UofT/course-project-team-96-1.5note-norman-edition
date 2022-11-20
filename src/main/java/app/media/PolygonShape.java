package app.media;

public class PolygonShape extends GenericShape {
    private int sideCount;
    private double startAngle;
    private double radius;

    public PolygonShape(double x, double y, double width, double height, String colour, double radius, double startAngle, int sideCount) {
        super("Polygon", x, y, 0, 0, colour);
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

    @Override
    public String toString() {
        return "Polygon: x["+super.getX()+"] y["+super.getY()+"] r["+this.getRadius()+"] a["+this.getStartAngle()+"] s["+this.getSideCount()+"] c["+super.getColour()+"]";
    }
}
