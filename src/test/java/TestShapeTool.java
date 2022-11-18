import app.media.RectangleShape;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.*;
import static org.junit.Assert.*;


import java.io.File;
import java.util.Set;

import app.media.Media;

public class TestShapeTool {
    double TOLERANCE = 0.00001;

    @Test
    public void testShapeCreationFromDimensions() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;
        GUIShape rect = new GUIRectangle(p1, p2, color);

        double x = rect.getMedia().getX();
        double y = rect.getMedia().getY();
        double w = rect.getMedia().getWidth();
        double h = rect.getMedia().getHeight();

        assertEquals(0, x - w/2, TOLERANCE);
        assertEquals(0, y - h/2, TOLERANCE);
        assertEquals(200, w, TOLERANCE);
        assertEquals(100, h, TOLERANCE);
    }


    @Test
    public void testSameShapeGivenSameInputs() {
        // Test a rectangle initialized with the same settings as another both have the same dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;
        GUIShape rect1 = new GUIRectangle(p1, p2, color);
        GUIShape rect2 = new GUIRectangle(p1, p2, color);

        String rect1desc = rect1.getMedia().toString();
        String rect2desc = rect2.getMedia().toString();
        assertEquals(rect1desc, rect2desc);
    }


}
