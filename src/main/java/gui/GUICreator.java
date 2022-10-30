package gui;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import gui.start_screen.StartScreen;
import gui.SwapPane;
import gui.tool.Tools;


public final class GUICreator {

    private GUICreator() {}

    public static Parent createGUI() {
        MenuBar menuBar = new MenuBar();
        SwapPane swapPane = new SwapPane();

        Node startScreen = new StartScreen(swapPane, menuBar, Tools.getTools());
        swapPane.setBase(startScreen);

        VBox vBox = new VBox(menuBar, swapPane);
        VBox.setVgrow(swapPane, Priority.ALWAYS);

        return vBox;
    }
}
