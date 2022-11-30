package gui.media;

import gui.tool.app.media.GenericShape;
import gui.tool.app.media.RectangleShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * An implementation of GUIShape representing a rectangle
 */

public class GUIRectangle extends GUIShape {
    private Rectangle rectangle;

    /**
     * Initializes and draws a GUIEllipse with the following settings
     * @param p1 The rectangle's top left corner
     * @param p2 The rectangle's bottom right corner
     * @param colour The rectangle's color
     */
    public GUIRectangle(Point2D p1, Point2D p2, Color colour) {
        super(new RectangleShape(0,0,0,0, colour.toString()));
        double[] result = RestrictPoints(p1, p2, false);
        double posX = result[0];
        double posY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating dimensions of GenericShape
        getMedia().setX(posX);
        getMedia().setY(posY);
        getMedia().setWidth(width);
        getMedia().setHeight(height);

        // Drawing Code
        rectangle = new Rectangle(-width/2, -height/2, width/2, height/2);
        rectangle.setFill(colour);
        getChildren().add(rectangle);
    }

    /**
     * Constructs a GUIRectangle from a RectangleShape
     * @param media The RectangleShape to base a new GUIRectangle off of
     */
    public GUIRectangle(RectangleShape media) {
        super(media);
        setGenericShape(media);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Must contain a generic RectangleShape for proper operation
     */
    @Override
    public void setGenericShape(GenericShape rectangle) {
        Color colour = Color.valueOf(rectangle.getColour());

        // Drawing Code
        double w = rectangle.getWidth();
        double h = rectangle.getHeight();
        this.rectangle = new Rectangle(-w/2,-h/2, w, h);
        this.rectangle.setFill(colour);
        getChildren().add(this.rectangle);
    }

    /**
     * {@inheritDoc}
     * @param p1 The position of the ellipse's anchored corner
     * @param p2 The position the shape's untethered corner
     */
    @Override
    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){
        double[] result = RestrictPoints(p1, p2, sameSideLengths);

        double centerX = result[0];
        double centerY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating the graphics of our shape
        getMedia().setX(centerX);
        getMedia().setY(centerY);
        rectangle.setX(-width/2);
        rectangle.setY(-height/2);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }
}
