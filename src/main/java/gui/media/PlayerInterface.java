package gui.media;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Represents the GUI elements compromising a media player
 */
public class PlayerInterface extends VBox {
    private final Button play, redo, forward;
    private final Slider playbackSlider, audioSlider;
    private final ComboBox<String> playRateOptions;
    private final Text audioLabel, playbackText;


    public PlayerInterface(String name) {
        //Creating visual elements related to managing play state of the mediaplayer
        this.play = new Button("Play");
        this.redo = new Button("Replay");
        this.forward = new Button("Fast Forward"); //A bit useless but it's for visual effect

        //Creating visual elements related to the play state of the mediaplayer
        this.playbackSlider = new Slider(0, 1, 0);
        playbackSlider.setPrefWidth(360);
        this.playbackText = new Text("00:00:00");

        //Creating visual elements related to the settings for the mediaplayer
        this.playRateOptions = new ComboBox<>();
        playRateOptions.getItems().addAll("0.5x", "1x", "1.5x", "2x");

        //Setting the default selection to the current desired playback rate
        playRateOptions.getSelectionModel().select(1);
        playRateOptions.setPrefWidth(70);

        this.audioSlider = new Slider(0, 1, 1);
        audioSlider.setPrefWidth(80);

        this.audioLabel = new Text(name);
        compileLayout();
    }

    /**
     * Compiles all UI elements into 1 layout
     */
    public void compileLayout() {
        HBox playManager = new HBox();
        playManager.getChildren().addAll(redo, play, forward);
        playManager.setSpacing(10);
        playManager.setAlignment(Pos.CENTER);

        HBox playSettingsBox = new HBox();
        playSettingsBox.getChildren().addAll(playbackSlider, playbackText);
        playSettingsBox.setSpacing(10);
        playSettingsBox.setAlignment(Pos.CENTER);

        VBox playBox = new VBox();
        playBox.getChildren().addAll(playManager, playSettingsBox);
        playBox.setSpacing(20);

        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(playRateOptions, audioLabel, audioSlider);
        bottomBox.setSpacing(80);
        bottomBox.setAlignment(Pos.CENTER);

        //Overall layout of the player
        VBox playerLayout = new VBox();
        playerLayout.getChildren().addAll(playBox, bottomBox);
        playerLayout.setSpacing(7.5);
        playerLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        playerLayout.setPadding(new Insets(7, 12, 7, 12));
        getChildren().add(playerLayout);
    }

    public Text getPlaybackText() {
        return playbackText;
    }

    public Slider getPlaybackSlider() {
        return playbackSlider;
    }

    public Button getForward() {
        return forward;
    }

    public Button getPlay() {
        return play;
    }

    public Button getRedo() {
        return redo;
    }

    public ComboBox<String> getPlayRateOptions() {
        return playRateOptions;
    }

    public Slider getAudioSlider() {
        return audioSlider;
    }
}
