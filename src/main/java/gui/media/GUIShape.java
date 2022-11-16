package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class GUIShape extends GUIMedia<GenericShape> {
    private GUIShape shape;

    public GUIShape(GenericShape media) {
        super(media);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){}
}

