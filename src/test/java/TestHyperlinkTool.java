package gui.tool;
import app.MediaCommunicator;
import gui.page.Page;
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

    public void testLinkAccess(){
        String s = new String ("s");
        String link = new String ("https://en.wikipedia.org/wiki/Javadoc");
        Point2D point = new Point2D(0,0);

        GUIHyperlinkBox b1 = new GUIHyperlinkBox(point, s,  link, Color.BLUE );




    }
}
