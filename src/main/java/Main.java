import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;


public class Main extends Application {

    private static String getMessage() {
        return "Come on, fhqwhgads.";
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(300);
        stage.setHeight(100);
        stage.setResizable(true);

        Text text = new Text(getMessage());

        String fileName = "04. Mirror of The World.mp3";
        Media sound = new Media(new File(fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        MediaAudio audio = new MediaAudio(new double[]{0, 0}, new double[]{0, 0}, 0, 0, 0, "",
                mediaPlayer, new byte[]{}, new ArrayList<MediaHyperlink>());

        GUIAudio audioGUI = new GUIAudio(audio);
        HBox layout = audioGUI.createInterface();

        Pane pane = new BorderPane(layout);

        stage.setScene(new Scene(pane));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
