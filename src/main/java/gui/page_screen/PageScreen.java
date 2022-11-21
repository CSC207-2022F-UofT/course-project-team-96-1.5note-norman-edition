package gui.page_screen;

import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;

import java.util.List;
import java.util.ArrayList;

import gui.SwapPane;
import gui.page.Page;
import gui.tool.ToolFactory;
import gui.tool.Tool;

import app.MediaCommunicator;


/**
 * GUI element which displays and allows modification to a page.
 * <p>
 * This is the main GUI element for the whole program.
 */
public class PageScreen extends VBox {

    private StackPane layers;
    private Tool[] tools;
    private Toolbar toolBar;
    private ToolPane toolPane;
    private Page page;
    private ScrollPane scrollPane;

    public PageScreen(MediaCommunicator c) {
        tools = ToolFactory.getTools();

        toolBar = new Toolbar(tools);
        getChildren().add(toolBar);

        layers = new StackPane();
        layers.setViewOrder(1);
        VBox.setVgrow(layers, Priority.ALWAYS);

        newPage(c);

        getChildren().add(layers);

        toolPane = new ToolPane(toolBar.selectedTool());
        addLayer(toolPane);
    }

    private void addLayer(Node layer) {
        layers.getChildren().add(layer);
    }

    public void newPage(MediaCommunicator c) {
        if (page != null) {
            page.setEventHandler(null);
            layers.getChildren().remove(scrollPane);
        }

        page = new Page(c);
        page.setEventHandler(toolBar.selectedTool().getValue());
        toolBar.selectedTool().addListener((o, oldVal, newVal) -> {
            // Set the currently selected tool to handle input events for the
            // page.
            page.setEventHandler(newVal);
        });
//        zoomableScrollPane = new ZoomableScrollPane(page);
//        zoomableScrollPane.setFitToHeight(true);
//        zoomableScrollPane.setFitToWidth(true);
//        zoomableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        zoomableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//
//        layers.getChildren().add(0, zoomableScrollPane);

        scrollPane = new ScrollPane(page);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        layers.getChildren().add(0, scrollPane);
    }

    public Page getPage() {
        return this.page;
    }
}
//    public ScrollPane getScrollPane() {return scrollPane;}
//}
