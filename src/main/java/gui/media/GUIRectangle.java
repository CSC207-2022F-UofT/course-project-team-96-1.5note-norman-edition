package gui.media;

import app.media.GenericShape;
import app.media.RectangleShape;
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
        getMedia().setX(posX - width/2);
        getMedia().setY(posY - height/2);
        getMedia().setWidth(width);
        getMedia().setHeight(height);

        rectangle = new Rectangle(0, 0, width, height);
        rectangle.setFill(colour);
        getChildren().add(rectangle);
    }

    /**
     *
     * @param media
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
        this.rectangle = new Rectangle(0,0, rectangle.getWidth(), rectangle.getHeight());
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
        getMedia().setX(centerX - width/2);
        getMedia().setY(centerY - height/2);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
    }
}
