package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class GUIPolygon extends GUIShape {
    private Polygon polygon;
    private int sideCount;
    public GUIPolygon(Point2D p1, Point2D p2, Color colour, int sideCount) {
        super(new GenericShape(Math.min(p1.getX(), p2.getX()),Math.min(p1.getY(), p2.getY()),0,0, colour.toString()));
        this.sideCount = sideCount;
        polygon = new Polygon();
        polygon.getPoints().addAll(calcPointsFromPoints(p1, p2, sideCount));
        polygon.setFill(colour);
        getChildren().add(polygon);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        polygon.getPoints().clear();
        polygon.getPoints().addAll(calcPointsFromPoints(p1, p2, sideCount));
    }

    public Double[] calcPointsFromPoints(Point2D p1, Point2D p2, int sideCount) {
        double radius = p1.distance(p2);
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        double angle = Math.atan2(deltaY, deltaX);
        Double[] points = new Double[2*sideCount];
        double angleStep = 360.0 / sideCount;
        for (int i = 0; i < sideCount; i++) {
            double currentAngle = Math.toRadians(i*angleStep) + angle;
            points[i*2] = radius * Math.cos(currentAngle);
            points[i*2+1] = radius * Math.sin(currentAngle);
        }
        return points;
    }
}
