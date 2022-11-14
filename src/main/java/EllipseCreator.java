import javafx.geometry.Point2D;
import java.lang.Math;
import javafx.scene.shape.Ellipse;


public class EllipseCreator extends ShapeCreator {
    public void useTwoPositions(Point2D first, Point2D second){
        double x1 = first.getX();
        double y1 = first.getY();
        double x2 = second.getX();
        double y2 = second.getY();
        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double radiusX = (x2 - x1) / 2;
        double radiusY = (y2 - y1) / 2;
        // Should end up creating a circle via Ellipse(double centerX, double centerY, double radiusX, double radiusY);
    }
}
