package gui.media;

import app.media.GenericShape;
import javafx.geometry.Point2D;

public class GUIShape extends GUIMedia<GenericShape> {

    public GUIShape(GenericShape media) {
        super(media);
    }

    public void update(Point2D p1, Point2D p2, boolean sameSideLengths){}

    public double[] RestrictPoints (Point2D origin, Point2D current, boolean sameSideLengths) {
        // Accepts two points and returns the dimensions of a shape to fill the space as follows:
        // horizontal midpoint, vertical midpoint, width, height
        double x1 = origin.getX();
        double y1 = origin.getY();
        double x2 = current.getX();
        double y2 = current.getY();

        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        if (sameSideLengths) {
            width = Math.min(width, height);
            height = width;
        }

        System.out.println(width+" "+height);

        x2 = clamp(x2, x1 - width, x1 + width);
        y2 = clamp(y2, y1 - height, y1 + height);

        return new double[]{(x1+x2)/2, (y1+y2)/2, width, height};
    }

    public double clamp(double value, double minimum, double maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }
}

