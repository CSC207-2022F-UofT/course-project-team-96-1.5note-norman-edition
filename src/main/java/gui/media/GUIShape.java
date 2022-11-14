package gui.media;

import app.media.PenStroke;
import app.media.Shape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class GUIShape extends GUIMedia<Shape> {
    public GUIShape(Point2D p1, Point2D p2, Color colour){
        super(new Shape(p1.getX(), p1.getY(), p2.getX() - p1.getX(), p2.getY() - p1.getY(), colour.toString()));
    }
    public GUIShape(Shape media) {
        super(media);
        //setStroke(media);
    }

}
