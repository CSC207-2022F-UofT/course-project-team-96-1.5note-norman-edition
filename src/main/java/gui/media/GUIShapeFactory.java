package gui.media;

import app.media.*;

public class GUIShapeFactory {
    public static GUIShape getFor(GenericShape shape) throws Exception {
        //System.out.println(shape);
        if (shape instanceof RectangleShape) {
            return new GUIRectangle((RectangleShape) shape);
        }
        else if (shape instanceof EllipseShape) {
            return new GUIEllipse((EllipseShape) shape);
        }
        else if (shape instanceof PolygonShape) {
            return new GUIPolygon((PolygonShape) shape);
        }
        else {
            throw new Exception("No appropriate GUIShape class for `" + shape + "`.");
        }
    }
}
