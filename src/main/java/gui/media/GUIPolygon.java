package gui.media;

import app.media.PolygonShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * An implementation of the GUIShape class representing a polygon
 * */
public class GUIPolygon extends GUIShape {
    private Polygon polygon;
    private int sideCount;
    public GUIPolygon(Point2D p1, Point2D p2, Color colour, int sideCount) {
        super(new PolygonShape(Math.min(p1.getX(), p2.getX()),Math.min(p1.getY(), p2.getY()),0,0, colour.toString(), 0, 0, sideCount));
        this.sideCount = sideCount;
        polygon = new Polygon();
        polygon.getPoints().addAll(calcPointsFromPoints(p1, p2, sideCount));
        polygon.setFill(colour);
        getChildren().add(polygon);
    }

    public GUIPolygon(PolygonShape media) {
        super(media);
        setGenericShape(media);
    }

    public void setGenericShape(PolygonShape polygon) {
        Color colour = Color.valueOf(polygon.getColour());
        this.polygon = new Polygon();
        this.polygon.getPoints().addAll(calcPointsFromRadiusAngle(polygon.getRadius(), polygon.getStartAngle(), polygon.getSideCount()));
        this.polygon.setFill(colour);
        getChildren().add(this.polygon);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        polygon.getPoints().clear();
        polygon.getPoints().addAll(calcPointsFromPoints(p1, p2, sideCount));

        // Update the polygonshape's data so that when it gets saved it's updated
        PolygonShape polygonshape = ((PolygonShape) getMedia());
        polygonshape.setRadius(p1.distance(p2));
        polygonshape.setStartAngle(calcAngle(p1, p2));
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
    public Double[] calcPointsFromRadiusAngle(double radius, double angle, int sideCount) {
        Double[] points = new Double[2*sideCount];
        double angleStep = 360.0 / sideCount;
        for (int i = 0; i < sideCount; i++) {
            double currentAngle = Math.toRadians(i*angleStep + angle);
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
    public Double[] calcPointsFromPoints(Point2D p1, Point2D p2, int sideCount) {
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
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }
}
