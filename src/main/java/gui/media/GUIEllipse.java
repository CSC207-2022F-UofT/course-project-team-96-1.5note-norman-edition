package gui.media;

import app.media.EllipseShape;
import app.media.GenericShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * An implementation of GUIShape representing an ellipse
 */
public class GUIEllipse extends GUIShape {
    private Ellipse ellipse;

    /**
     * Initializes and draws a GUIEllipse with the following settings
     * @param p1 The ellipse's top left corner
     * @param p2 The ellipse's bottom right corner
     * @param colour The ellipse's color
     */
    public GUIEllipse(Point2D p1, Point2D p2, Color colour) {
        super(new EllipseShape(p1, p2, colour.toString()));

        ellipse = new Ellipse(0, 0, 0, 0);
        ellipse.setFill(colour);
        update(p1, p2, false);
        getChildren().add(ellipse);
    }

    /**
     * Constructs a GUIEllipse from a EllipseShape
     * @param media The EllipseShape to base a new GUIEllipse off of
     */
    public GUIEllipse(EllipseShape media) {
        super(media);
        setGenericShape(media);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Must contain a generic EllipseShape for proper operation
     */
    @Override
    public void setGenericShape(GenericShape ellipse) {
        Color colour = Color.valueOf(ellipse.getColour());

        this.ellipse = new Ellipse(0,0,0,0);
        update(ellipse.getP1(), ellipse.getP2(), false);
        this.ellipse.setFill(colour);
        getChildren().add(this.ellipse);
    }

    /**
     * Specific implementation of updatePoints for GUIEllipse
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
    public void update(Point2D p1, Point2D p2, boolean sameSideLengths) {
        double[] result = RestrictPoints(p1, p2, sameSideLengths);

        double centerX = result[0];
        double centerY = result[1];
        double width = result[2];
        double height = result[3];

        // Updating the graphics of our shape
        getMedia().setX(centerX); // RestrictPoints returns center position by default
        getMedia().setY(centerY); // RestrictPoints returns center position by default
        getMedia().setP1(CornerTL(centerX, centerY, width, height));
        getMedia().setP2(CornerBR(centerX, centerY, width, height));
        ellipse.setRadiusX(width/2);
        ellipse.setRadiusY(height/2);
    }

}
