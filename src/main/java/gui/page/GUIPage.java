package gui.page;

import javafx.scene.layout.*;
import javafx.beans.value.*;

import app.MediaCommunicator;
import app.Page;


public class GUIPage extends StackPane implements Page {

    private MediaCommunicator c;
    private ObservableValue<GUIPageTool> t;

    public GUIPage(MediaCommunicator c, ObservableValue<GUIPageTool> t) {
        this.c = c;
        this.t = t;

        c.addPage(this);
        t.addListener((o, oldVal, newVal) -> {
            setCursor(newVal.getCursor());
        });
        getStyleClass().add("page");
    }

    public void close() {
        c.removePage(this);
    }
}
