import javafx.geometry.Point2D;
import java.lang.Math;
import javafx.scene.shape.Rectangle;

public class RectCreator extends ShapeCreator {
    public void useTwoPositions(Point2D first, Point2D second){
        double x1 = first.getX();
        double y1 = first.getY();
        double x2 = second.getX();
        double y2 = second.getY();
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);
        // Should end up creating a rectangle via Rectangle(x1, y1, width, height);
    }
}
