package gui.page;

import javafx.scene.layout.*;
import javafx.beans.value.*;

import app.MediaCommunicator;
import app.Page;


public class GUIPage extends StackPane implements Page {

    private MediaCommunicator c;
    private ObservableValue<? extends GUIPageTool> t;

    public GUIPage(
            MediaCommunicator c, ObservableValue<? extends GUIPageTool> t)
    {
        this.c = c;
        this.t = t;

        c.addPage(this);
        getStyleClass().add("page");
    }

    public void close() {
        c.removePage(this);
    }
}
