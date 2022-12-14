package gui.page_screen;

import javafx.scene.*;
import javafx.scene.layout.*;

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

    private final StackPane layers;
    private final Toolbar toolBar;
    private Page page;

    public PageScreen(MediaCommunicator c) {
        Tool[] tools = ToolFactory.getTools();

        toolBar = new Toolbar(tools);
        getChildren().add(toolBar);

        layers = new StackPane();
        layers.setViewOrder(1);
        VBox.setVgrow(layers, Priority.ALWAYS);

        newPage(c);

        getChildren().add(layers);


        ToolPane toolPane = new ToolPane(toolBar.selectedTool());
        addLayer(toolPane);
    }

    private void addLayer(Node layer) {
        layers.getChildren().add(layer);
    }

    public void newPage(MediaCommunicator c) {
        closePage();

        page = new Page(c);
        page.setEventHandler(toolBar.selectedTool().getValue());
        toolBar.selectedTool().addListener((o, oldVal, newVal) -> {
            // Set the currently selected tool to handle input events for the
            // page.
            page.setEventHandler(newVal);
        });

        layers.getChildren().add(0, page);
    }

    public void closePage() {
        if (page != null) {
            page.setEventHandler(null);
            layers.getChildren().remove(page);
            page.removeAllMedia();
        }
    }

    public Page getPage() {
        return page;
    }
}
