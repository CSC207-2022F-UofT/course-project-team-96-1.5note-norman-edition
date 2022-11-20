import app.MediaCommunicator;
import app.media.GenericShape;
import gui.media.GUIEllipse;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import gui.page.Page;
import gui.tool.ColourTool;
import gui.tool.ShapeTool;
import gui.tool.ShapeType;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.*;
import static org.junit.Assert.*;
import storage.SQLiteStorage;

public class TestShapeTool {
    static double TOLERANCE = 0.00001;

    // JavaFX related stuff
    static boolean init = false;

    static void initJfxRuntime() {
        // Necessary in order to run JavaFX in a JUnit test
        if (!init) {
            Platform.startup(() -> {});
            init = true;
        }
    }

    static void testBounds(GUIShape shape, double xPos, double yPos, double width, double height){
        // Tests whether the shape's position and dimensions match with what is expected
        double x = shape.getMedia().getX();
        double y = shape.getMedia().getY();
        double w = shape.getMedia().getWidth();
        double h = shape.getMedia().getHeight();

        assertEquals(xPos, x, TOLERANCE);
        assertEquals(yPos, y, TOLERANCE);
        assertEquals(width, w, TOLERANCE);
        assertEquals(height, h, TOLERANCE);
    }

    // Beginning of tests
    // TODO: Find how to trigger autocalculation of shape dimensions
    @Test
    public void testGUIRectangleShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;
        GUIShape rect = new GUIRectangle(p1, p2, color);

        testBounds(rect,0, 0, 200, 100);
    }

    @Test
    public void testGUIEllipseShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(200,100);
        Color color = Color.RED;
        GUIShape rect = new GUIEllipse(p1, p2, color);

        testBounds(rect,0, 0, 200, 100);
    }

    @Test
    public void testShapeCreationFromTool() throws Exception {
        // Tests whether a shape created via the tool will have proper dimensions
        // In this test, we test with an ellipse
        initJfxRuntime();

        // Initialize relevant tools and classes to support them
        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
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
        double w = guishape.getMedia().getWidth();
        double h = guishape.getMedia().getHeight();

        assertEquals(0, x, TOLERANCE);
        assertEquals(0, y, TOLERANCE);
        assertEquals(200, w, TOLERANCE);
        assertEquals(100, h, TOLERANCE);
    }

}
