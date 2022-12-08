import app.MediaCommunicator;
import gui.media.GUIHyperlinkBox;
import gui.page.Page;
import gui.tool.ColourTool;
import gui.tool.HyperlinkTool;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.Test;
import storage.SQLiteStorage;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestHyperlinkTool {

    static double TOLERANCE = 0.00001;

    static boolean init = false;
    static void initJfxRuntime() {
        try {
            // Necessary in order to run JavaFX in a JUnit test
            if (!init) {
                System.out.println("Init JFX");
                Platform.startup(() ->
                {
                    // This block will be executed on JavaFX Thread
                });
                init = true;
            }
        } catch (IllegalStateException e) {}
    }

    static Point2D randomPoint() {
        // generates a random point
        Random rand = new Random();
        double px = rand.nextDouble(100);
        double py = rand.nextDouble(100);
        return new Point2D(px, py);
    }

    @Test
    public void testLinks(){
        // Test initialization of a hyperlink box
        String goodLink = "https://en.wikipedia.org/wiki/Javadoc";
        String badLink = "w";

        String s = "s";
        Color colour = Color.BLUE;
        Point2D point = new Point2D(0,0);
        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  goodLink, colour );
        GUIHyperlinkBox b2 = new GUIHyperlinkBox(point, s, badLink, colour);
        assertEquals(goodLink, b1.getLink());
        assertEquals (badLink, b2.getLink() );

    }

    @Test
    public void testText(){
        // tests text attribute of hyperlink box
        String link = "https://en.wikipedia.org/wiki/Javadoc";
        String s = "words";
        Color colour = Color.BLUE;
        Point2D point = new Point2D(0,0);
        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  link, colour );
        assertEquals(s, b1.getText());

    }

    @Test
    public void testCheckLink(){
        // tests the checkLink method in the HyperlinkTool class
        String link = "https://www.businessinsider.com/guides/tech/discord-text-formatting";
        assertTrue(HyperlinkTool.checkLink(link));
    }

    @Test
    public void testGetName() throws Exception{
        // tests the getName method in the HyperlinkTool class

        initJfxRuntime();

        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
        ColourTool ct = new ColourTool();
        HyperlinkTool ht = new HyperlinkTool(ct.colourProperty());
        ht.enabledFor(page);
        Point2D point = new Point2D (0.0, 0.0);
        ht.makeHyperlink(point, "words", "https://www.statisticshowto.com/memoryless-property/", Color.BLUE);
        assertEquals(ht.getName(), "Hyperlink");
    }

    @Test
    public void testPosition() throws Exception{
        // tests position of hyperlink box

        initJfxRuntime();

        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
        ColourTool ct = new ColourTool();
        HyperlinkTool ht = new HyperlinkTool(ct.colourProperty());
        ht.enabledFor(page);
        Point2D point = randomPoint();
        ht.makeHyperlink(point, "words","https://www.statisticshowto.com/memoryless-property/", Color.BLUE);
        GUIHyperlinkBox hb = ht.getCurrentHyperlinkBox();

        double x = hb.getMedia().getX();
        double y = hb.getMedia().getY();

        assertEquals(point.getX(), x, TOLERANCE );
        assertEquals(point.getY(), y, TOLERANCE);
    }

    @Test
    public void testMakeHyperlink() throws Exception {
        // test the making of a hyperlink with an invalid link
        initJfxRuntime();

        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
        ColourTool ct = new ColourTool();
        HyperlinkTool ht = new HyperlinkTool(ct.colourProperty());
        ht.enabledFor(page);

        Point2D point = randomPoint();

        assertTrue(ht.makeHyperlink(point,
                "words", "https://www.statisticshowto.com/memoryless-property/", Color.BLUE));
    }

    static void testBounds(GUIHyperlinkBox box, double xPos, double yPos){
        // Tests whether the hyperlink's position match with what is expected
        // helper method
        double x = box.getMedia().getX();
        double y = box.getMedia().getY();

        assertEquals(xPos, x, TOLERANCE);
        assertEquals(yPos, y, TOLERANCE);

    }
}
