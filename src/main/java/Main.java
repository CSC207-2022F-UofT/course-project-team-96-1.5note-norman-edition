import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;

import gui.GUICreator;


public class Main extends Application {

    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT= 500;
    private static final int INITIAL_WIDTH = 1000;
    private static final int INITIAL_HEIGHT = 700;

    @Override
    public void start(Stage stage) throws Exception {
        Parent gui = GUICreator.createGUI();

        Scene scene = new Scene(gui);
        scene.getStylesheets().add("file:res/css/base.css");

        stage.setScene(scene);
        stage.setWidth(INITIAL_WIDTH);
        stage.setHeight(INITIAL_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
