package gui.page_screen;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;

import gui.SwapPane;
import gui.page.GUIPageTool;
import gui.page.GUIPage;

import app.Page;
import app.MediaCommunicator;


/**
 * GUI element which displays and allows modification to a page.
 * <p>
 * This is the main GUI element for the whole program.
 */
public class PageScreen extends VBox {

    private StackPane layers;
    private GUIPage page;
    private Toolbar toolBar;
    private ToolPane toolPane;

    public PageScreen(GUIPageTool[] tools, MediaCommunicator c) {
        toolBar = new Toolbar(tools);
        getChildren().add(toolBar);


        page = new GUIPage(c, toolBar.selectedTool());

        layers = new StackPane(page);
        VBox.setVgrow(layers, Priority.ALWAYS);

        getChildren().add(layers);


        toolPane = new ToolPane(toolBar.selectedTool());
        addLayer(toolPane);
    }

    private void addLayer(Node layer) {
        layers.getChildren().add(layer);
    }

    /**
     * Run cleanup tasks.
     * <p>
     * This method should be called <i>at most</i> once, and the GUIPage,
     * PageScreen, and/or any of their contents should not be used afterwards.
     */
    public void close() {
        page.close();
    }
}
