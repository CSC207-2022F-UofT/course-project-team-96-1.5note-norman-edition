package gui.media;

import app.media.GenericShape;
import app.media.PolygonShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * An implementation of the GUIShape class representing a polygon
 * */
public class GUIPolygon extends GUIShape {
    private Polygon polygon;

    /**
     * The number of sides of the polygon
     */
    private int sideCount;

    /**
     * Initializes and draws GUIPolygon with the following settings
     * @param p1 The position of the polygon's center
     * @param p2 The position which determines the polygon's radius and starting angle
     * @param colour The polygon's color
     * @param sideCount The number of sides of the polygon
     */
    public GUIPolygon(Point2D p1, Point2D p2, Color colour, int sideCount) {
        super(new PolygonShape(p1,p2, colour.toString(), 0, 0, sideCount));

        ((PolygonShape) getMedia()).setRadius(p1.distance(p2));
        ((PolygonShape) getMedia()).setStartAngle(calcAngle(p1, p2));

        this.sideCount = sideCount;

        polygon = new Polygon();
        polygon.setFill(colour);
        update(p1, p2, false);
        getChildren().add(polygon);
    }

    /**
     * Constructs a GUIEllipse from a EllipseShape
     * @param media The EllipseShape to base a new GUIEllipse off of
     */
    public GUIPolygon(PolygonShape media) {
        super(media);
        setGenericShape(media);
    }


    /**
     * {@inheritDoc}
     * <p>
     * Must contain a generic PolygonShape for proper operation
     */
    @Override
    public void setGenericShape(GenericShape polygon) {
        PolygonShape poly = (PolygonShape) polygon;
        Color colour = Color.valueOf(polygon.getColour());

        this.sideCount = poly.getSideCount();
        this.polygon = new Polygon();
        update(poly.getP1(), poly.getP2(), false);
        this.polygon.setFill(colour);
        getChildren().add(this.polygon);
    }

    /**
     * Specific implementation of updatePoints for GUIPolygon
     */
    @Override
    public void updatePoints() {
        Point2D p1 = getMedia().getP1();
        Point2D p2 = getMedia().getP2();

        double prevCenterX = p1.getX();
        double prevCenterY = p1.getY();

        double centerX = getMedia().getX();
        double centerY = getMedia().getY();

        Point2D diff = new Point2D(centerX, centerY)
                .subtract(new Point2D(prevCenterX, prevCenterY));

        getMedia().setP1(p1.add(diff));
        getMedia().setP2(p2.add(diff));
    }

    /**
     * {@inheritDoc}
     * @param p1 The position of the polygon's center
     * @param p2 The position which determines the polygon's radius and starting angle
     * @param sameSideLengths Has no effect for a polygon
     */
    @Override
    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        polygon.getPoints().clear();
        polygon.getPoints().addAll(calcPointsFromPoints(p1, p2, sideCount));
        // Update the polygonshape's data so that when it gets saved it's updated
        PolygonShape polygonshape = ((PolygonShape) getMedia());
        polygonshape.setRadius(p1.distance(p2));
        polygonshape.setStartAngle(calcAngle(p1, p2));
        getMedia().setX(p1.getX());
        getMedia().setY(p1.getY());
        getMedia().setP1(p1);
        getMedia().setP2(p2);
    }

    /**
     * Generates a list of points representing the vertices of a polygon with an arbitrary orientation and dimensions.
     * <p>
     * This always creates a Regular Shape, which is a shape with equal side lengths.
     * @param radius The distance from the
     * @param angle The rotational offset of the polygon
     * @param sideCount The number of sides of the polygon
     * @return A list of points representing the polygon's vertices
     */
    public static Double[] calcPointsFromRadiusAngle(double radius, double angle, int sideCount) {
        Double[] points = new Double[2*sideCount];
        double angleStep = 360.0 / sideCount;
        // Iterate through every angle and create a vertex for each
        for (int i = 0; i < sideCount; i++) {
            double currentAngle = Math.toRadians(i * angleStep + angle);
            // JavaFX accepts an array of (x,y) pairs e.x. [x0,y0,x1,y1,...xn,yn]
            points[i*2] = radius * Math.cos(currentAngle);
            points[i*2+1] = radius * Math.sin(currentAngle);
        }
        return points;
    }
    /**
     * Generates a list of points representing the vertices of a polygon with an arbitrary orientation and dimensions.
     * <p>
     * This always creates a Regular Shape, which is a shape with equal side lengths.
     * @param p1 The location of the center of the polygon
     * @param p2 The location of the top most vertex of the polygon (used for determining direction)
     * @param sideCount The number of sides of the polygon
     * @return A list of points representing the polygon's vertices
     */
    public static Double[] calcPointsFromPoints(Point2D p1, Point2D p2, int sideCount) {
        double radius = p1.distance(p2);
        double angle = calcAngle(p1, p2);
        return calcPointsFromRadiusAngle(radius, angle, sideCount);
    }

    /**
     * Returns the angle of the vector connecting two points
     * @param p1 The tail of the vector
     * @param p2 The head of the vector
     * @return The angle of the vector connecting two points
     */
    public static double calcAngle(Point2D p1, Point2D p2) {
        // Calculates the angle of the vector drawn from point 1 to point 2
        double deltaX = p2.getX() - p1.getX(); // Horizontal difference
        double deltaY = p2.getY() - p1.getY(); // Vertical difference
        return Math.toDegrees(Math.atan2(deltaY, deltaX)); // Angle opposite from Vertical side of right triangle
    }
}
