package gui.media;

import app.media.GenericShape;
import app.media.Media;
import app.media.PenStroke;
import app.media.PolygonShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

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
        setPolygon(media);
    }

    @Override
    public void mediaUpdated(Media media) {
        PolygonShape newPolygon = (PolygonShape) media;
        PolygonShape currentPolygon = (PolygonShape) getMedia();

        if (currentPolygon != newPolygon) {
            setMedia(newPolygon);
            setPolygon(newPolygon);
        }
    }

    public void setPolygon(PolygonShape polygon) {
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
    public Double[] calcPointsFromPoints(Point2D p1, Point2D p2, int sideCount) {
        double radius = p1.distance(p2);
        double angle = calcAngle(p1, p2);
        return calcPointsFromRadiusAngle(radius, angle, sideCount);
    }

    public static double calcAngle(Point2D p1, Point2D p2) {
        // Calculates the angle of the vector drawn from point 1 to point 2
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }
}
