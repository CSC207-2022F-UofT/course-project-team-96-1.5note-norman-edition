package gui.tool;
import javafx.geometry.*;
import gui.media.GUIHyperlinkBox;
import javafx.scene.paint.*;

public class TestHyperlinkTool {

    public void testLinkAccess(){
        String s = new String ("s");
        String link = new String ("https://en.wikipedia.org/wiki/Javadoc");
        Point2D point = new Point2D(0,0);

        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  link, Color.BLUE );




    }
}
