import app.MediaCommunicator;
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
    double TOLERANCE = 0.00001;

    // JavaFX related stuff
    static boolean init = false;

    static void initJfxRuntime() {
        // Necessary in order to run JavaFX in a JUnit test
        if (!init) {
            Platform.startup(() -> {});
            init = true;
        }
    }

    // Beginning of tests
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

        assertEquals(0, x, TOLERANCE);
        assertEquals(0, y, TOLERANCE);
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
