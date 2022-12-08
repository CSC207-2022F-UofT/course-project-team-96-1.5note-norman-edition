import gui.page.Page;
import app.MediaCommunicator;
import javafx.application.Platform;
import org.junit.*;
import static org.junit.Assert.*;
import storage.SQLiteStorage;

/** Only testing the zooming methods in Page since we can't simulate mouse clicks or scrolling
 *
 */

public class TestZoomScroll {

    static Page page;

    static void initJfxRuntime() {
        // Necessary in order to run JavaFX in a JUnit test
        try {
            System.out.println("Init JFX");
            Platform.startup(() ->
            {
                // This block will be executed on JavaFX Thread
            });
        } catch (IllegalStateException e) {}
    }

    public static void createPage() throws Exception {
        initJfxRuntime();

        SQLiteStorage s = new SQLiteStorage(null);
        MediaCommunicator c = new MediaCommunicator(s);
        page = new Page(c);
    }


    @Test
    public void testZoomToFactor() throws Exception {
        // test that zoomToFactor changes both scaleFactor and scale's X and Y values
        createPage();

        page.zoomToFactor(1.5);
        assertEquals(1.5, page.getScaleFactor(), 0);
        assertEquals(1.5, page.getScale().getX(), 0);
        assertEquals(1.5, page.getScale().getY(), 0);

        page.zoomToFactor(0.1);
        assertEquals(0.1, page.getScaleFactor(), 0);
        assertEquals(0.1, page.getScale().getX(), 0);
        assertEquals(0.1, page.getScale().getY(), 0);

        page.zoomToFactor(1.0);
        assertEquals(1.0, page.getScaleFactor(), 0);
        assertEquals(1.0, page.getScale().getX(), 0);
        assertEquals(1.0, page.getScale().getY(), 0);
    }

    @Test
    public void testZoomInOrOut() throws Exception {
        // test that zoomInOrOut changes both scaleFactor and scale's X and Y values in both directions
        createPage();

        page.zoomInOrOut("In");
        assertEquals(1.5, page.getScaleFactor(), 0);
        assertEquals(1.5, page.getScale().getX(), 0);
        assertEquals(1.5, page.getScale().getY(), 0);

        // test that zoomInOrOut finds the right "next" value when beginning with a double not in
        // {0.1, 0.25, 1.0/3.0, 0.5, 2.0/3.0, 0.75,
        // 1.0, 1.5, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}

        page.zoomToFactor(3.5);
        page.zoomInOrOut("Out");
        assertEquals(3.0, page.getScaleFactor(), 0);
        assertEquals(3.0, page.getScale().getX(), 0);
        assertEquals(3.0, page.getScale().getY(), 0);
    }

    @Test
    public void testMinMaxZooms() throws Exception {
        // test that zooming in on max (10.0) zoom and zooming out on min (0.1) zoom doesn't change anything
        // however, zooming out on max and zooming in on min should still work
        createPage();

        page.zoomToFactor(10.0);
        page.zoomInOrOut("In");
        assertEquals(10.0, page.getScaleFactor(), 0);
        assertEquals(10.0, page.getScale().getX(), 0);
        assertEquals(10.0, page.getScale().getY(), 0);
        page.zoomInOrOut("Out");
        assertEquals(9.0, page.getScaleFactor(), 0);
        assertEquals(9.0, page.getScale().getX(), 0);
        assertEquals(9.0, page.getScale().getY(), 0);

        page.zoomToFactor(0.1);
        page.zoomInOrOut("Out");
        assertEquals(0.1, page.getScaleFactor(), 0);
        assertEquals(0.1, page.getScale().getX(), 0);
        assertEquals(0.1, page.getScale().getY(), 0);
        page.zoomInOrOut("In");
        assertEquals(0.25, page.getScaleFactor(), 0);
        assertEquals(0.25, page.getScale().getX(), 0);
        assertEquals(0.25, page.getScale().getY(), 0);
    }

    @Test
    public void testScrolling() throws Exception {
        // test correctness of scrollVertically, scrollHorizontally, and centerPage
        createPage();

        page.scrollVertically(10.0);
        page.scrollHorizontally(10.0);
        assertEquals(10.0, page.getMediaLayer().getTranslateY(), 0);
        assertEquals(10.0, page.getMediaLayer().getTranslateX(), 0);

        page.centerPage();
        assertEquals(0.0, page.getMediaLayer().getTranslateY(), 0);
        assertEquals(0.0, page.getMediaLayer().getTranslateX(), 0);

        page.scrollVertically(-10.0);
        page.scrollHorizontally(-10.0);
        assertEquals(-10.0, page.getMediaLayer().getTranslateY(), 0);
        assertEquals(-10.0, page.getMediaLayer().getTranslateX(), 0);
    }

    @Test
    public void testZoomScrollInteraction() throws Exception {
        // test correctness of scrollVertically and scrollHorizontally when zoomed in
        createPage();

        page.zoomToFactor(10.0);
        page.scrollVertically(10.0);
        page.scrollHorizontally(10.0);
        assertEquals(10.0, page.getMediaLayer().getTranslateY(), 0);
        assertEquals(10.0, page.getMediaLayer().getTranslateX(), 0);

        // test correctness of zoomToFactor when scrolled
        page.zoomToFactor(5.0);
        assertEquals(5.0, page.getScaleFactor(), 0);
        assertEquals(5.0, page.getScale().getX(), 0);
        assertEquals(5.0, page.getScale().getY(), 0);
    }
}
