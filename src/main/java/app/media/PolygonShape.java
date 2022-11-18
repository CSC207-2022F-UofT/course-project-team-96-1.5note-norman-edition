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

    public double getRadius() {
        return radius;
    }

    public double getStartAngle() {
        return startAngle;
    }
    public int getSideCount() {
        return sideCount;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public void setSideCount(int sideCount) {
        this.sideCount = sideCount;
    }

    @Override
    public String toString() {
        return "Polygon: x["+super.getX()+"] y["+super.getY()+"] r["+this.getRadius()+"] a["+this.getStartAngle()+"] s["+this.getSideCount()+"] c["+super.getColour()+"]";
    }
}
