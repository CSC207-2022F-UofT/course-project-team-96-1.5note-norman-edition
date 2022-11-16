package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class GUIEllipse extends GUIShape {
    private Ellipse ellipse;

    public GUIEllipse(Point2D p1, Point2D p2, Color colour) {
        // TODO: Find out why colour isn't applied, and why nonzero positions leads to an offset ellipse
        super(new GenericShape((p1.getX() + p2.getX()) / 2,(p1.getY() + p2.getY()) / 2,0,0, colour.toString()));
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        ellipse = new Ellipse(0, 0, width / 2, height / 2);
        ellipse.setFill(colour);
        getChildren().add(ellipse);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths) {
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

        // Updating the graphics of our shape
        getMedia().setX(p1.getX() + width / 2);
        getMedia().setY(p1.getY() + height / 2);
        ellipse.setRadiusX(width/2);
        ellipse.setRadiusY(height/2);
    }
}
