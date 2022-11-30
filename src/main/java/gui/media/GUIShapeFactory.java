package gui.media;

import gui.tool.app.media.EllipseShape;
import gui.tool.app.media.GenericShape;
import gui.tool.app.media.PolygonShape;
import gui.tool.app.media.RectangleShape;

/**
 * Instantiate the correct GUIMedia sub-class for a given GenericShape object.
 * <p>
 * Determines the appropriate GenericShape subclass to create, given a GenericShape
 * Used as a helper class for the GUIMediaFactory
 */
public class GUIShapeFactory {
    /**
     * Returns the GUIMedia representation of a GenericShape class
     * @param shape The GenericShape to be represented
     * @return The GUIMedia class representing the GenericShape provided
     * @throws Exception Thrown if shape is not a valid type of GenericShape
     */
    public static GUIShape getFor(GenericShape shape) throws Exception {
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
