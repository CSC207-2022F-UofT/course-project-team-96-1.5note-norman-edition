import app.MediaCommunicator;
import app.media.PolygonShape;
import gui.media.GUIEllipse;
import gui.media.GUIPolygon;
import gui.media.GUIRectangle;
import gui.media.GUIShape;
import gui.page.Page;
import gui.tool.ColourTool;
import gui.tool.ShapeTool;
import gui.tool.ShapeType;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

    static double randomBounded(double min, double max) {
        // Returns a random double bounded between two doubles
        return ((Math.random() * (max - min)) + min);
    }
    static Point2D randomPoint(double xMin, double yMin, double xMax, double yMax) {
        double px = randomBounded(xMin, xMax);
        double py = randomBounded(yMin, yMax);
        return new Point2D(px, py);
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

        // Pseudo simulation of mouse being dragged
        rect.update(randomPoint(-100, -100, 100, 100), randomPoint(-100, -100, 100, 100), false);
        rect.update(p1, p2, false);

        testBounds(rect,100, 50, 200, 100);
    }

    @Test
    public void testGUIEllipseShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(0,0);
        Point2D p2 = new Point2D(-100,-200);
        Color color = Color.RED;
        GUIShape ellipse = new GUIEllipse(p1, p2, color);

        // Pseudo simulation of mouse being dragged
        ellipse.update(randomPoint(-300, -300, 300, 300), randomPoint(-300, -300, 300, 300), false);
        ellipse.update(p1, p2, false);

        testBounds(ellipse,-50, -100, 100, 200);
    }

    @Test
    public void testGUIPolygonShapeCreation() {
        // Test initialization of a rectangle with the proper position and dimensions
        Point2D p1 = new Point2D(100,50);
        Point2D p2 = new Point2D(100,55);
        Color color = Color.RED;
        GUIShape poly = new GUIPolygon(p1, p2, color, 4);

        // Pseudo simulation of mouse being dragged
        poly.update(randomPoint(-100, -100, 100, 100), randomPoint(-100, -100, 100, 100), false);
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

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Hello World!");
        StackPane root = new StackPane();
        root.getChildren().add(page);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

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
