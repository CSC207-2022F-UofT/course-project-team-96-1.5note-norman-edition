package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GUIRectangle extends GUIShape {
    private Rectangle rectangle;

    public GUIRectangle(Point2D p1, Point2D p2, Color colour) {
        super(new GenericShape(Math.min(p1.getX(), p2.getX()),Math.min(p1.getY(), p2.getY()),0,0, colour.toString()));
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        rectangle = new Rectangle(0, 0, width, height);
        rectangle.setFill(colour);
        getChildren().add(rectangle);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        if (sameSideLengths) {
            width = Math.min(width, height);
            height = width;
        }
        double cornerX = Math.min(p1.getX(), p2.getX());
        double cornerY = Math.min(p1.getY(), p2.getY());

        // Updating the graphics of our shape
        getMedia().setX(cornerX);
        getMedia().setY(cornerY);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }
}
