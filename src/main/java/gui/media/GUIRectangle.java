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
        super(new RectangleShape(p1, p2, colour.toString()));

        rectangle = new Rectangle(0,0,0,0);
        rectangle.setFill(colour);
        update(p1, p2, false);
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

        this.rectangle = new Rectangle(0,0,0,0);
        update(rectangle.getP1(), rectangle.getP2(), false);
        this.rectangle.setFill(colour);
        getChildren().add(this.rectangle);
    }

    /**
     * Specific implementation of updatePoints for GUIRectangle
     */
    public void updatePoints() {
        Point2D p1 = getMedia().getP1();
        Point2D p2 = getMedia().getP2();

        double[] result = RestrictPoints(p1, p2, false);
        double prevCenterX = result[0];
        double prevCenterY = result[1];

        double centerX = getMedia().getX();
        double centerY = getMedia().getY();

        Point2D diff = new Point2D(centerX, centerY)
                .subtract(new Point2D(prevCenterX, prevCenterY));

        getMedia().setP1(p1.add(diff));
        getMedia().setP2(p2.add(diff));
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
        getMedia().setP1(CornerTL(centerX, centerY, width, height));
        getMedia().setP2(CornerBR(centerX, centerY, width, height));
        this.rectangle.setX(-width/2);
        this.rectangle.setY(-height/2);
        this.rectangle.setWidth(width);
        this.rectangle.setHeight(height);
    }
}
