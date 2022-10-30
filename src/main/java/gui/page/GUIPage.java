package gui.page;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;

import gui.SwapPane;

import app.Page;
import app.MediaCommunicator;


/**
 * GUI element which displays and allows modification to a page.
 * <p>
 * This is the main GUI element for the whole program.
 */
public class GUIPage extends VBox implements Page {

    private StackPane layers;
    private Pane canvas;
    private GUIPageToolbar toolBar;
    private GUIPageToolPane toolPane;

    private MediaCommunicator c;

    public GUIPage(GUIPageTool[] tools, MediaCommunicator c) {
        this.c = c;

        toolBar = new GUIPageToolbar(tools);
        getChildren().add(toolBar);


        canvas = new Pane();
        canvas.getStyleClass().add("page");

        layers = new StackPane(canvas);
        VBox.setVgrow(layers, Priority.ALWAYS);

        getChildren().add(layers);


        toolPane = new GUIPageToolPane(toolBar.selectedTool());
        addLayer(toolPane);


        toolBar.selectedTool().addListener((o, oldVal, newVal) -> {
            canvas.setCursor(newVal.getCursor());
        });
    }

    private void addLayer(Node layer) {
        layers.getChildren().add(layer);
    }

    /**
     * Run cleanup tasks.
     * <p>
     * This method should be called <i>at most</i> once, and the GUIPage should
     * not be used anymore afterwards.
     */
    public void close() {
        c.removePage(this);
    }
}
