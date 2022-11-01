package gui;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import gui.start_screen.StartScreen;
import gui.SwapPane;


public final class GUICreator {

    private GUICreator() {}

    public static Parent createGUI() {
        MenuBar menuBar = new MenuBar();
        SwapPane swapPane = new SwapPane();

        Node startScreen = new StartScreen(swapPane, menuBar);
        swapPane.setBase(startScreen);

        VBox vBox = new VBox(menuBar, swapPane);
        VBox.setVgrow(swapPane, Priority.ALWAYS);

        return vBox;
    }
}
