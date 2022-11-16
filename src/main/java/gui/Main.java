package gui;

import java.net.URL;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;


/**
 * Entrypoint for the whole program.
 * <p>
 * This class extends the Application class from JavaFX which allows it to
 * initialize and populate the main application window.
 */
public class Main extends Application {

    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT= 500;
    private static final int INITIAL_WIDTH = 1000;
    private static final int INITIAL_HEIGHT = 700;

    @Override
    public void start(Stage stage) throws Exception {
        // create the GUI contents
        Parent gui = GUICreator.createGUI();

        // create a scene containing the GUI. The scene is the "context" in
        // which the rest of the GUI exists and from which various properties
        // are inherited, i.e. to load a font or a stylesheet for the GUI, we
        // load it into the scene so that it gets applied to all the contents
        // of that scene.
        Scene scene = new Scene(gui);

        // Load the stylesheets
        URL cssURL = ResourceLoader.getResourceURL("css/base.css");
        scene.getStylesheets().add(cssURL.toString());

        // The stage is the main window for the application. To actually get
        // the GUI on the screen, we add the scene in which it is contained to
        // the stage.
        stage.setScene(scene);

        // Set the dimensions of the wnidow.
        stage.setWidth(INITIAL_WIDTH);
        stage.setHeight(INITIAL_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // Finally, make the window visible after setting it up.
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
