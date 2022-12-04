import app.media.EllipseShape;
import app.media.PolygonShape;
import app.media.RectangleShape;
import gui.media.GUIEllipse;
import gui.media.GUIPolygon;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import gui.page.Page;
import gui.tool.ColourTool;
import gui.tool.ShapeTool;
import gui.tool.ShapeType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.*;
import static org.junit.Assert.*;

public class TestShapeTool {
    static double TOLERANCE = 0.00001;

    static Point2D randomPoint() {
        double px = Math.random();
        double py = Math.random();
        return new Point2D(px, py);
    }

    @Test
    public void testGUIRectangleShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;
        GUIShape rect = new GUIRectangle(p1, p2, color);

        // Pseudo simulation of mouse being dragged
        rect.update(randomPoint(), randomPoint(), false);
        rect.update(randomPoint(), randomPoint(), false);
        rect.update(p1, p2, false);

        assertEquals(100, rect.getMedia().getX(), TOLERANCE);
        assertEquals(50, rect.getMedia().getY(), TOLERANCE);
    }

    @Test
    public void testGUIEllipseShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(-100,-200);
        Color color = Color.RED;
        GUIShape ellipse = new GUIEllipse(p1, p2, color);

        // Pseudo simulation of mouse being dragged
        ellipse.update(randomPoint(), randomPoint(), false);
        ellipse.update(randomPoint(), randomPoint(), false);
        ellipse.update(p1, p2, false);

        assertEquals(-50, ellipse.getMedia().getX(), TOLERANCE);
        assertEquals(-100, ellipse.getMedia().getY(), TOLERANCE);
    }

    @Test
    public void testGUIPolygonShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(100,50);
        Point2D p2 = new Point2D(100,55);
        Color color = Color.RED;
        GUIShape poly = new GUIPolygon(p1, p2, color, 4);

        // Pseudo simulation of mouse being dragged
        poly.update(randomPoint(), randomPoint(), false);
        poly.update(randomPoint(), randomPoint(), false);
        poly.update(p1, p2, false);

        assertEquals(100, poly.getMedia().getX(), TOLERANCE);
        assertEquals(50, poly.getMedia().getY(), TOLERANCE);
        assertEquals(5, ((PolygonShape) poly.getMedia()).getRadius(), TOLERANCE);
        assertEquals(4, ((PolygonShape) poly.getMedia()).getSideCount(), TOLERANCE);
        // With how atan2 is defined, providing a perfectly vertical displacement will give you 90.
        assertEquals(90, ((PolygonShape) poly.getMedia()).getStartAngle(), TOLERANCE);
    }

    @Test
    public void testShapeCreationFromTool() throws Exception {
        // Initialize relevant tools and classes to support them
        Page page = TestGUI.createPage();
        ColourTool ct = new ColourTool();
        ShapeTool st = new ShapeTool(ct.colourProperty());
        st.enabledFor(page);

        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;

        st.startShape(p1, p2, color, ShapeType.ELLIPSE);
        GUIShape guishape = st.getCurrentShape();

        double x = guishape.getMedia().getX();
        double y = guishape.getMedia().getY();

        assertEquals(100, x, TOLERANCE);
        assertEquals(50, y, TOLERANCE);

        // Now test updating
        st.updateShape(randomPoint(), randomPoint(), true);
        st.updateShape(p1, p2, false);
        st.endShape();

        assertEquals(100, x, TOLERANCE);
        assertEquals(50, y, TOLERANCE);
    }

    @Test
    public void testGUIPolygonPointCalculation(){
        // Test that polygons are "regular" shapes (check this through equal side lengths and radii)
        double radius = 50;
        int sidecount = 9;
        Double[] points = GUIPolygon.calcPointsFromPoints(new Point2D(0,0), new Point2D(0,radius), sidecount);
        Point2D origin = new Point2D(0,0);
        // With how the function is defined, the last and the first points generated are neighbors (wraps around)
        Point2D currentPoint = new Point2D(points[0], points[1]);
        Point2D lastPoint = new Point2D(points[points.length-2], points[points.length - 1]);
        double side_length = currentPoint.distance(lastPoint); // Hypothetical side length
        for (int i = 0; i < sidecount; i++) {
            // JavaFX accepts an array of (x,y) pairs e.x. [x0,y0,x1,y1,...xn,yn]
            double x = points[i*2];
            double y = points[i*2+1];
            Point2D point = new Point2D(x,y);
            assertEquals(radius, point.distance(origin), TOLERANCE);

            currentPoint = point;
            assertEquals(side_length, currentPoint.distance(lastPoint), TOLERANCE);
            lastPoint = currentPoint;
        }
    }

    @Test
    public void GUIRectangleMediaRelated() {
        GUIRectangle rect = new GUIRectangle(new Point2D(0,0), new Point2D(0, 0), Color.RED);

        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        RectangleShape newRect = new RectangleShape(p1, p2, Color.RED.toString());
        rect.mediaUpdated(newRect);

        assertEquals(100, rect.getMedia().getX(), TOLERANCE);
        assertEquals(50, rect.getMedia().getY(), TOLERANCE);

        // Test that the media based alternative constructor works the same
        GUIRectangle rect2 = new GUIRectangle(newRect);
        assertEquals(rect.toString(), rect2.toString());
    }

    @Test
    public void GUIEllipseMediaRelated() {
        GUIEllipse ellipse = new GUIEllipse(new Point2D(0,0), new Point2D(0, 0), Color.RED);

        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        EllipseShape newEllipse = new EllipseShape(p1, p2, Color.RED.toString());
        ellipse.mediaUpdated(newEllipse);

        assertEquals(100, ellipse.getMedia().getX(), TOLERANCE);
        assertEquals(50, ellipse.getMedia().getY(), TOLERANCE);

        // Test that the media based alternative constructor works the same
        GUIEllipse ellipse2 = new GUIEllipse(newEllipse);
        assertEquals(ellipse.toString(), ellipse2.toString());
    }

    @Test
    public void testGUIPolygonMediaRelated() {
        int sideCount = 4;
        double radius = 5;

        GUIPolygon poly = new GUIPolygon(new Point2D(0,0), new Point2D(0,0), Color.RED, sideCount);

        Point2D p1 = new Point2D(100,50);
        Point2D p2 = new Point2D(100,50+radius);
        PolygonShape newPoly = new PolygonShape(p1, p2, Color.RED.toString(), radius, 0, sideCount);
        poly.mediaUpdated(newPoly);

        assertEquals(100, poly.getMedia().getX(), TOLERANCE);
        assertEquals(50, poly.getMedia().getY(), TOLERANCE);
        assertEquals(5, ((PolygonShape) poly.getMedia()).getRadius(), TOLERANCE);
        assertEquals(4, ((PolygonShape) poly.getMedia()).getSideCount(), TOLERANCE);
        // With how atan2 is defined, providing a perfectly vertical displacement will give you 90.
        assertEquals(90, ((PolygonShape) poly.getMedia()).getStartAngle(), TOLERANCE);

        // Test that the media based alternative constructor works the same
        GUIPolygon poly2 = new GUIPolygon(newPoly);
        assertEquals(poly.toString(), poly2.toString());
    }

    @Test
    public void testShapeToolMisc() throws Exception {
        Page page = TestGUI.createPage();
        ColourTool ct = new ColourTool();
        ShapeTool st = new ShapeTool(ct.colourProperty());

        // Test enabling/disabling works any number of times
        st.enabledFor(page);
        st.disabledFor(page);
        st.enabledFor(page);

        // Test tool is initialized properly on startup
        assertEquals("Shape",st.getName());
        assertNull(st.getCurrentShape());
        st.getGraphic();
        st.getSettingsGUI();
        assertNotNull(st.getHandlerMethods());

        // Test methods properly skip if the current shape is null
        st.endShape();
        st.updateShape(randomPoint(), randomPoint(), true);
        assertNull(st.getCurrentShape());
    }
}
