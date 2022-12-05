package app.media;

import javafx.geometry.Point2D;

import java.io.Serializable;

/**
 * A sub-class of Media representing an arbitrary shape
 */
public abstract class GenericShape extends Media {
    private String colour;
    private Point p1;
    private Point p2;


    /**
     * Initializes a Generic Shape with the given parameters
     * @param type The shape's type
     * @param p1 The first point the shape is defined by
     * @param p2 The second point the shape is defined by
     * @param colour The shape's color
     */
    public GenericShape(String type, Point2D p1, Point2D p2, String colour) {
        super(type, p1.getX(), p1.getY(), 0, 0);
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
        this.colour = colour;
    }

    private static record Point(double x, double y) implements Serializable {
        Point(Point2D p1){
            this(p1.getX(), p1.getY());
        }

        Point2D intoPoint2D(){
            return new Point2D(x(), y());
        }
    }

    /**
     * Gets the first point defining the shape
     * @return the first point defining the shape
     */
    public Point2D getP1() {
        return p1.intoPoint2D();
    }

    /**
     * Gets the second point defining the shape
     * @return the second point defining the shape
     */
    public Point2D getP2() {
        return p2.intoPoint2D();
    }

    /**
     * Returns the color of the shape
     * @return the color of the shape
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the first point defining the shape
     * @param p1 the first point defining the shape
     */
    public void setP1(Point2D p1) {
        this.p1 = new Point(p1);
    }

    /**
     * Sets the second point defining the shape
     * @param p2 the second point defining the shape
     */
    public void setP2(Point2D p2) {
        this.p2 = new Point(p2);
    }

    /**
     * Sets the colour of the shape
     * @param colour The desired color to set the shape to
     */
    public void setColour(String colour) {
        this.colour = colour;
    }
}

