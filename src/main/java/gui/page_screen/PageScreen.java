package gui.page_screen;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;

import java.util.List;
import java.util.ArrayList;

import gui.SwapPane;
import gui.page.Page;
import gui.tool.Tools;
import gui.tool.Tool;

import app.MediaCommunicator;


/**
 * GUI element which displays and allows modification to a page.
 * <p>
 * This is the main GUI element for the whole program.
 */
public class PageScreen extends VBox {

    private StackPane layers;
    private Toolbar toolBar;
    private ToolPane toolPane;

    public PageScreen(MediaCommunicator c) {
        Tool[] tools = Tools.getTools(c);

        toolBar = new Toolbar(tools);
        getChildren().add(toolBar);


        Page page = new Page(c);
        toolBar.selectedTool().addListener((o, oldVal, newVal) -> {
            // Set the currently selected tool to handle input events for the
            // page.
            page.setEventHandler(newVal);
        });

        layers = new StackPane(page);
        VBox.setVgrow(layers, Priority.ALWAYS);

        getChildren().add(layers);


        toolPane = new ToolPane(toolBar.selectedTool());
        addLayer(toolPane);
    }

    private void addLayer(Node layer) {
        layers.getChildren().add(layer);
    }
}
