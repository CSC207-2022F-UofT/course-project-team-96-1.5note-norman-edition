import app.MediaCommunicator;
import gui.media.GUIShape;
import gui.page.Page;
import gui.tool.ColourTool;
import gui.tool.HyperlinkTool;
import gui.tool.ShapeTool;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventTarget;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import gui.media.GUIHyperlinkBox;
import javafx.scene.paint.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;
import storage.SQLiteStorage;

public class TestHyperlinkTool {

    /*String s = new String ("s");
    Color colour = Color.BLUE;
    Point2D point = new Point2D(0,0);
    String link = new String ("https://en.wikipedia.org/wiki/Javadoc");

    assertEquals(link, b1.getLink());*/
    Color color = Color.BLUE;
    static double TOLERANCE = 0.00001;



    static boolean init = false;
    static void initJfxRuntime() {
        // Necessary in order to run JavaFX in a JUnit test
        if (!init) {
            System.out.println("Init JFX");
            Platform.startup(() ->
            {
                // This block will be executed on JavaFX Thread
            });
            init = true;
        }
    }

    @Test
    public void testLinks(){
        // Test initialization of a hyperlink box
        String goodLink = new String ("https://en.wikipedia.org/wiki/Javadoc");
        String badLink = new String ("w");

        String s = new String ("s");
        Color colour = Color.BLUE;
        Point2D point = new Point2D(0,0);
        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  goodLink, colour );
        GUIHyperlinkBox b2 = new GUIHyperlinkBox(point, s, badLink, colour);
        assertEquals(goodLink, b1.getLink());
        assertEquals (badLink, b2.getLink() );

    }

    @Test
    public void testText(){
        String link = new String ("https://en.wikipedia.org/wiki/Javadoc");
        String s = new String ("words");
        Color colour = Color.BLUE;
        Point2D point = new Point2D(0,0);
        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  link, colour );
        assertEquals(s, b1.getText());

    }

    @Test
    public void testCheckLink(){
        String link = new String ("https://www.businessinsider.com/guides/tech/discord-text-formatting");
        assertEquals(true, HyperlinkTool.checkLink(link));
    }

    @Test
    public void testMakeHyperlink() throws Exception{

        initJfxRuntime();

        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
        ColourTool ct = new ColourTool();
        HyperlinkTool ht = new HyperlinkTool(ct.colourProperty());
        ht.enabledFor(page);
        Double x =  0.0;
        Double y = 0.0;
        ht.makeHyperlink(x,y);
    }

    @Test
    public void testGetName()throws Exception{
        SQLiteStorage sqls = new SQLiteStorage(null);
        MediaCommunicator mc = new MediaCommunicator(sqls);
        Page page = new Page(mc);
        ColourTool ct = new ColourTool();
        HyperlinkTool ht = new HyperlinkTool(ct.colourProperty());
        ht.enabledFor(page);
        assertEquals (ht.getName(), "Hyperlink");
    }

    static void testBounds(GUIHyperlinkBox box, double xPos, double yPos){
        // Tests whether the shape's position and dimensions match with what is expected
        double x = box.getMedia().getX();
        double y = box.getMedia().getY();

        assertEquals(xPos, x, TOLERANCE);
        assertEquals(yPos, y, TOLERANCE);

    }
}
