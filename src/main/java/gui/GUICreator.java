package gui;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import gui.start_screen.StartScreen;
import gui.SwapPane;


/**
 * Utility class which instantiates main GUI components.
 */
public final class GUICreator {

    private GUICreator() {}

    public static Parent createGUI() {
        MenuBar menuBar = new MenuBar();
        SwapPane swapPane = new SwapPane();
        swapPane.setViewOrder(1);

        Node startScreen = new StartScreen(swapPane, menuBar);
        swapPane.setBase(startScreen);

        VBox vBox = new VBox(menuBar, swapPane);
        VBox.setVgrow(swapPane, Priority.ALWAYS);

        return vBox;
    }
}
